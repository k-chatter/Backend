package com.sum.chatter.service.auth;

import com.sum.chatter.dto.UserSignUpDto;
import com.sum.chatter.dto.auth.KakaoMeResponse;
import com.sum.chatter.dto.auth.NaverMeResponse;
import com.sum.chatter.dto.auth.OauthResponseDto;
import com.sum.chatter.repository.UserRepository;
import com.sum.chatter.repository.entity.User;
import com.sum.chatter.service.auth.oauth_client.KakaoOauthClient;
import com.sum.chatter.service.auth.oauth_client.NaverOauthClient;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final KakaoOauthClient kakaoOauthClient;

    private final NaverOauthClient naverOauthClient;

    private final JwtBuilder jwtBuilder;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public OauthResponseDto oauthKakao(String authCode) {
        String accessToken = kakaoOauthClient.postToken(authCode);
        KakaoMeResponse me = kakaoOauthClient.getInfo(accessToken);
        Optional<User> user = userRepository.findByOauthId(me.getOauthId());

        if (user.isEmpty()) {
            return OauthResponseDto.builder()
                    .statusCode(777) // if 777 code returned, client progress sign-up process;
                    .user(UserSignUpDto.builder()
                            .build())
                    .build();
        }

        JwtInfo jwtInfo = new JwtInfo(user.get().getId());

        return OauthResponseDto.builder()
                .statusCode(200)
                .user(null)
                .token(jwtBuilder.createJwt(jwtInfo))
                .build();
    }

    public OauthResponseDto oauthNaver(String authCode) {
        String accessToken = naverOauthClient.postToken(authCode);
        NaverMeResponse me = naverOauthClient.getInfo(accessToken);
        Optional<User> user = userRepository.findByOauthId(me.getOauthId());

        if (user.isEmpty()) {
            return OauthResponseDto.builder()
                    .statusCode(777) // if 777 code returned, client progress sign-up process;
                    .user(modelMapper.map(me, UserSignUpDto.class))
                    .build();
        }

        JwtInfo jwtInfo = new JwtInfo(user.get().getId());

        return OauthResponseDto.builder()
                .statusCode(200)
                .user(null)
                .token(jwtBuilder.createJwt(jwtInfo))
                .build();
    }

}
