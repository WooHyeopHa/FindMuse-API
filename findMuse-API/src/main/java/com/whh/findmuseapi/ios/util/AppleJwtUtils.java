package com.whh.findmuseapi.ios.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.whh.findmuseapi.common.constant.ResponseCode;
import com.whh.findmuseapi.common.exception.CBadRequestException;
import com.whh.findmuseapi.common.exception.CAccessTokenException;
import com.whh.findmuseapi.ios.config.AppleProperties;
import com.whh.findmuseapi.ios.dto.key.ApplePublicKey;
import com.whh.findmuseapi.ios.dto.key.ApplePublicKeys;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class AppleJwtUtils {
    
    private final AppleProperties appleProperties;
    
    /**
     * User가 Sign in with Apple 요청(<a href="https://appleid.apple.com/auth/authorize">...</a>)으로 전달 받은 identity token을 이용한 최초 검증
     *
     * @param idToken
     * @return SignedJWT
     */
    public SignedJWT verifyIdentityToken(String idToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            ReadOnlyJWTClaimsSet payload = signedJWT.getJWTClaimsSet();
            
            // EXP(만료기간) 검증
            Date currentTime = new Date(System.currentTimeMillis());
            if (!currentTime.before(payload.getExpirationTime())) {
                throw new CBadRequestException("잘못된 요청입니다. id_token을 다시 요청해주세요 : " + idToken);
            }
            
            // ISS, AUD 검증
            String AUD = appleProperties.getClientId();
            String AUTH_URL = appleProperties.getAuthUrl();
            if (!AUTH_URL.equals(payload.getIssuer()) | !AUD.equals(payload.getAudience().get(0))) {
                throw new CBadRequestException("잘못된 요청입니다. id_token을 다시 요청해주세요 : " + idToken);
            }
            
            return signedJWT;
        } catch (ParseException e) {
            throw new CAccessTokenException(ResponseCode.PARSE_EXCEPTION, "Token(id_token) : " + idToken);
        }
    }
    
    /**
     * id_token에서 추출한 alg, kid와 일치하는 alg, kid를 가진 publicKey 찾기
     * publicKey를 통해 RSAPublicKey 생성
     *
     * @param signedJWT, keys
     * @return PublicKey
     */
    public PublicKey generatePublicKey(SignedJWT signedJWT, ApplePublicKeys keys) {
        try {
            JWSHeader header = signedJWT.getHeader();
            log.info("token header : " + header);
            ApplePublicKey applePublicKey = keys.getMatchedKey(header.getKeyID(), header.getAlgorithm().getName());
            
            byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.getE());
            
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));
            
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
            
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new CAccessTokenException(ResponseCode.NO_SUCH_ALGORITHM, e.getMessage());
        } catch (InvalidKeySpecException e) {
            throw new CAccessTokenException(ResponseCode.INVALID_KEY_SPEC, e.getMessage());
        }
    }
    public ReadOnlyJWTClaimsSet getTokenClaims(SignedJWT signedJWT, PublicKey publicKey) {
        try {
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
            
            if (!signedJWT.verify(verifier)) {
                throw new CAccessTokenException(ResponseCode.TOKEN_INVALID);
            }
            
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new CAccessTokenException(ResponseCode.PARSE_EXCEPTION, "Token : " + signedJWT.toString());
        } catch (JOSEException e) {
            throw new CAccessTokenException(ResponseCode.JOSE_EXCEPTION, e.getMessage());
        }
    }
}
