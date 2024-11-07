package com.whh.findmuseapi.user.util;

import com.whh.findmuseapi.common.constant.Infos;
import com.whh.findmuseapi.user.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.whh.findmuseapi.user.repository.UserRepository; // UserRepository 임포트

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // 사용자 정보 생성
//        User user = User.builder()
//                .email("eeehhhggg@gmail.com")
//                .nickname("이혁규")
//                .role(Infos.Role.USER)
//                .accountId("test_user2")
//                .refreshToken("testRefreshToken2")
//                .build();

        // 사용자 저장
//        userRepository.save(user);
    }
}
