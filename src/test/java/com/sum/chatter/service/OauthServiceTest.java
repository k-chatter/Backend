package com.sum.chatter.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sum.TestFixture;
import com.sum.chatter.dto.UserSignUpDto;
import com.sum.chatter.dto.auth.KakaoMeResponse;
import com.sum.chatter.dto.auth.NaverMeResponse;
import com.sum.chatter.dto.auth.OauthResponseDto;
import com.sum.chatter.repository.UserRepository;
import com.sum.chatter.repository.entity.User;
import com.sum.chatter.service.auth.JwtBuilder;
import com.sum.chatter.service.auth.JwtInfo;
import com.sum.chatter.service.auth.OauthService;
import com.sum.chatter.service.auth.oauth_client.KakaoOauthClient;
import com.sum.chatter.service.auth.oauth_client.NaverOauthClient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Transactional
public class OauthServiceTest {

    private final OauthService oauthService;

    private final KakaoOauthClient kakaoOauthClient;

    private final NaverOauthClient naverOauthClient;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final JwtBuilder jwtBuilder;

    @Autowired
    public OauthServiceTest(UserRepository userRepository) {
        this.kakaoOauthClient = Mockito.mock(KakaoOauthClient.class);
        this.naverOauthClient = Mockito.mock(NaverOauthClient.class);
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
        this.jwtBuilder = new JwtBuilder("abcdabcdabcdabcdabcdabcdabcdabcd");
        this.oauthService = new OauthService(kakaoOauthClient, naverOauthClient, jwtBuilder, userRepository,
                modelMapper);
    }

    @Test
    public void 유저_존재_카카오_인증_성공() {
        //given
        User user = userRepository.save(TestFixture.getUser());
        Mockito.doReturn("accessToken").when(kakaoOauthClient).postToken("authCode");
        Mockito.doReturn(KakaoMeResponse.builder().id("13").build()).when(kakaoOauthClient).getInfo("accessToken");

        //when
        OauthResponseDto result = oauthService.oauthKakao("authCode");

        //then
        assertEquals(200, result.getStatusCode());
        assertNull(result.getUser());
        assertEquals(jwtBuilder.createJwt(new JwtInfo(user.getId())), result.getToken());
    }

    @Test
    public void 유저_없음_카카오_인증_성공() {
        //given
        Mockito.doReturn("accessToken").when(kakaoOauthClient).postToken("authCode");
        Mockito.doReturn(KakaoMeResponse.builder().id("13").build()).when(kakaoOauthClient).getInfo("accessToken");

        //when
        OauthResponseDto result = oauthService.oauthKakao("authCode");

        //then
        assertEquals(777, result.getStatusCode());
        assertEquals(UserSignUpDto.builder().build(), result.getUser());
        assertNull(result.getToken());
    }

    @Test
    public void 유저_존재_네이버_인증_성공() {
        //given
        User user = userRepository.save(TestFixture.getUser());
        NaverMeResponse me = TestFixture.getNaverMeResponse();
        Mockito.doReturn("accessToken").when(naverOauthClient).postToken("authCode");
        Mockito.doReturn(me).when(naverOauthClient).getInfo("accessToken");

        //when
        OauthResponseDto result = oauthService.oauthNaver("authCode");

        //then
        assertEquals(200, result.getStatusCode());
        assertNull(result.getUser());
        assertEquals(jwtBuilder.createJwt(new JwtInfo(user.getId())), result.getToken());
    }

    @Test
    public void 유저_없음_네이버_인증_성공() {
        //given
        NaverMeResponse me = TestFixture.getNaverMeResponse();
        Mockito.doReturn("accessToken").when(naverOauthClient).postToken("authCode");
        Mockito.doReturn(me).when(naverOauthClient).getInfo("accessToken");

        //when
        OauthResponseDto result = oauthService.oauthNaver("authCode");

        //then
        assertEquals(777, result.getStatusCode());
        assertEquals(modelMapper.map(me, UserSignUpDto.class), result.getUser());
        assertNull(result.getToken());
    }

}
