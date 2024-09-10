package com.whh.findmuseapi.ios.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.whh.findmuseapi.common.Exception.CustomBadRequestException;
import com.whh.findmuseapi.common.constant.Infos.Role;
import com.whh.findmuseapi.ios.dto.AppleRevokeRequest;
import com.whh.findmuseapi.ios.dto.key.ApplePublicKeys;
import com.whh.findmuseapi.jwt.service.JwtService;
import com.whh.findmuseapi.ios.config.AppleProperties;
import com.whh.findmuseapi.ios.dto.AppleLoginResponse;
import com.whh.findmuseapi.ios.dto.AppleToken;
import com.whh.findmuseapi.ios.feign.AppleAuthClient;
import com.whh.findmuseapi.ios.util.AppleJwtUtils;
import com.whh.findmuseapi.user.entity.User;
import com.whh.findmuseapi.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@EnableConfigurationProperties({ AppleProperties.class })
@RequiredArgsConstructor
@Service
public class AppleService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    
    private final AppleProperties appleProperties;
    private final AppleAuthClient appleAuthClient;
    private final AppleJwtUtils appleJwtUtils;
    
    public ReadOnlyJWTClaimsSet getTokenClaims(String identityToken) {
        SignedJWT signedJWT = appleJwtUtils.verifyIdentityToken(identityToken);
        ApplePublicKeys applePublicKeys = appleAuthClient.getAppleAuthPublicKey();
        PublicKey publicKey = appleJwtUtils.generatePublicKey(signedJWT, applePublicKeys);
        
        return appleJwtUtils.getTokenClaims(signedJWT, publicKey);
    }
    public User login(AppleLoginResponse appleLoginResponse) {
        try {
            ReadOnlyJWTClaimsSet jwtClaimsSet = getTokenClaims(appleLoginResponse.getIdToken());
            
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject payload = objectMapper.readValue(jwtClaimsSet.toJSONObject().toJSONString(), JSONObject.class);
            
            // 유저 정보 추출
            String accountId = String.valueOf(payload.get("sub"));
            log.info("accountId" + accountId);
            String email = String.valueOf(payload.get("email"));
            
            User findUser = userRepository.findByAccountId(accountId).orElse(null);
            
            if (findUser == null) {
                // 신규 회원가입의 경우 DB에 저장
                return userRepository.save(
                    User.builder()
                        .accountId(accountId)
                        .email(email)
                        .role(Role.GUEST)
                        .refreshToken(jwtService.createRefreshToken())
                        .build()
                );
            }
            
            return findUser;
            
        } catch (JsonProcessingException e) {
            throw new CustomBadRequestException(appleLoginResponse.getIdToken());
        }
    }
    
    private String createClientSecretKey(String clientId) {
        // headersParams 적재
        Map<String, Object> headerParamsMap = new HashMap<>();
        headerParamsMap.put("kid", appleProperties.getLoginKey());
        headerParamsMap.put("alg", "ES256");
        
        // clientSecretKey 생성
        try {
            return Jwts
                .builder()
                .setHeader(headerParamsMap)
                .setIssuer(appleProperties.getTeamId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30))
                .setAudience(appleProperties.getAuthUrl())
                .setSubject(clientId)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
        } catch (IOException e) {
            throw new CustomBadRequestException("Apple 키 파일이 인식되지 않음 : " + appleProperties.getKeyPath());
        }
    }
    
    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(appleProperties.getKeyPath());
        String privateKey = new String(resource.getInputStream().readAllBytes());
        
        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
        
        return converter.getPrivateKey(privateKeyInfo);
    }
    
    private AppleToken.Response generateAuthToken(String code) {
        if (code == null) throw new IllegalArgumentException();
        
        String clientId = appleProperties.getClientId();
        return appleAuthClient.getToken(AppleToken.Request.builder()
            .code(code)
            .client_id(clientId)
            .client_secret(createClientSecretKey(clientId))
            .grant_type("authorization_code")
            .refresh_token(null)
            .build());
    }
    
    public void deleteAppleAccount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        deleteUserAcount(user);
        
        AppleRevokeRequest appleRevokeRequest = AppleRevokeRequest.builder()
            .client_id(appleProperties.getClientId())
            .client_secret(createClientSecretKey(appleProperties.getClientId()))
            .token(user.getRefreshToken())
            .token_type_hint("refresh_token")
            .build();
        
        String responseBody = appleAuthClient.revoke(appleRevokeRequest);
        
        log.info("애플 연결해제 요청 결과 : " + responseBody);
    }
    
    public void deleteUserAcount(User user) {
        userRepository.delete(user);
    }

    public User extractUserFromAccessToken(String accessToken) {
        Optional<String> email = jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, accessToken);
        if (email.isEmpty()) {
            throw new CustomBadRequestException("토큰에서 이메일을 추출할 수 없습니다. 제공된 액세스 토큰: " + accessToken);
        }
        
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new CustomBadRequestException("이메일로 사용자를 찾을 수 없습니다. 제공된 이메일: " + email.get());
        }
        
        return user.get();
    }
    
    public User loginWithToken(AppleLoginResponse appleLoginResponse) {
        return login(appleLoginResponse);
    }
    
    public void loginSuccess(User user, HttpServletResponse response) {
        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(user.getEmail(), refreshToken);
    }
}
