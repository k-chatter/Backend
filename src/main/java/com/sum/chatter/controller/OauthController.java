package com.sum.chatter.controller;

import com.sum.chatter.dto.auth.OauthResponseDto;
import com.sum.chatter.service.auth.OauthService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oAuthService;

    @GetMapping("kakao")
    public OauthResponseDto oauthKakao(HttpServletRequest request) {
        String authCode = exportAuthCode(request.getQueryString());

        System.out.println("Called KakaoController");
        return oAuthService.oauthKakao(authCode);
    }

    @GetMapping("naver")
    public OauthResponseDto oauthNaver(HttpServletRequest request) {
        String authCode = exportAuthCode(request.getQueryString());

        return oAuthService.oauthNaver(authCode);
    }

    private String exportAuthCode(String queryString) {
        List<String> queries = Arrays.stream(queryString.split("&")).toList();
        Map<String, String> queryMap = queries.stream().map(str -> str.split("="))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));

        return queryMap.get("code");
    }

}
