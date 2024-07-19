package com.sum.chatter.service.auth.oauth_client;

import com.sum.chatter.dto.auth.NaverMeResponse;
import com.sum.chatter.dto.auth.NaverMeResponseDto;
import com.sum.chatter.dto.auth.NaverTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;


@Component
public class NaverOauthClient implements OauthClient {

    private static final String OAUTH_URI = "https://nid.naver.com/oauth2.0/token";

    private static final String API_URI = "https://openapi.naver.com";

    private final RestClient client = RestClient.builder().build();

    private final String clientId;

    private final String clientSecret;

    private final String redirectUri;

    public NaverOauthClient(@Value("${oauth.naver.client_id}") String clientId,
                            @Value("${oauth.naver.client_secret}") String clientSecret,
                            @Value("${oauth.redirect_url}") String redirectHost) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = String.format("http://%s/oauth/naver", redirectHost);
    }

    @Override
    public String postToken(String authCode) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("grant_type", "authorization_code");
        param.add("client_id", clientId);
        param.add("client_secret", clientSecret);
        param.add("redirect_uri", redirectUri);
        param.add("code", authCode);

        ResponseEntity<NaverTokenResponse> result = client.post()
                .uri(OAUTH_URI)
                .body(param)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new RestClientException("RestClient Exception: " + response.getStatusCode() + " " +  response.getBody());
                }))
                .toEntity(NaverTokenResponse.class);

        if (result.getBody() == null) {
            throw new RestClientException("RestClient Exception: body is null");
        }

        return result.getBody().getAccessToken();
    }

    @Override
    public NaverMeResponse getInfo(String accessToken) {
        ResponseEntity<NaverMeResponseDto> result = client.get()
                .uri(API_URI + "/v1/nid/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new RestClientException("RestClient Exception: " + response.getStatusCode() + " " +  response.getBody());
                }))
                .toEntity(NaverMeResponseDto.class);

        if (result.getBody() == null) {
            throw new RestClientException("RestClient Exception: body is null");
        }

        return result.getBody().getResponse();
    }

}
