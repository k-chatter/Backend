package com.sum.chatter.service.auth;

import com.sum.chatter.dto.UserSignUpDto;
import com.sum.chatter.dto.auth.OAuthResponseDto;
import com.sum.chatter.repository.UserRepository;
import com.sum.chatter.repository.entity.User;
import com.sum.chatter.dto.auth.KakaoMeResponse;
import com.sum.chatter.service.auth.oauth_client.KakaoOAuthClient;
import com.sum.chatter.dto.auth.NaverMeResponse;
import com.sum.chatter.service.auth.oauth_client.NaverOAuthClient;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final KakaoOAuthClient kakaoOAuthClient;

    private final NaverOAuthClient naverOAuthClient;

    private final JwtBuilder jwtBuilder;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public OAuthResponseDto oauthKakao(String authCode) {
        String accessToken = kakaoOAuthClient.postToken(authCode);
        KakaoMeResponse me = kakaoOAuthClient.getInfo(accessToken);
        Optional<User> user = userRepository.findByOauthId(me.getId());

        if (user.isEmpty()) {
            return OAuthResponseDto.builder()
                    .statusCode(777) // if 777 code returned, client progress sign-up process;
                    .user(UserSignUpDto.builder()
                            .build())
                    .build();
        }

        JwtInfo jwtInfo = new JwtInfo(user.get().getId());

        return OAuthResponseDto.builder()
                .statusCode(200)
                .user(null)
                .token(jwtBuilder.createJwt(jwtInfo))
                .build();
    }

    public OAuthResponseDto oauthNaver(String authCode) {
        String accessToken = naverOAuthClient.postToken(authCode);
        NaverMeResponse me = naverOAuthClient.getInfo(accessToken);
        Optional<User> user = userRepository.findByOauthId(me.getId());

        if (user.isEmpty()) {
            return OAuthResponseDto.builder()
                    .statusCode(777) // if 777 code returned, client progress sign-up process;
                    .user(modelMapper.map(me, UserSignUpDto.class))
                    .build();
        }

        JwtInfo jwtInfo = new JwtInfo(user.get().getId());

        return OAuthResponseDto.builder()
                .statusCode(200)
                .user(null)
                .token(jwtBuilder.createJwt(jwtInfo))
                .build();

    }
}
