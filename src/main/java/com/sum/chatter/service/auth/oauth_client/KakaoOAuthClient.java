package com.sum.chatter.service.auth.oauth_client;

import com.sum.chatter.dto.auth.KakaoMeResponse;
import com.sum.chatter.dto.auth.KakaoTokenResponse;
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
public class KakaoOAuthClient implements OAuthClient {

    private static final String OAUTH_URI = "https://kauth.kakao.com/oauth/token";

    private static final String API_URI = "https://kapi.kakao.com";

    private final RestClient client = RestClient.builder().build();

    private final String clientId;

    private final String redirectUri;

    public KakaoOAuthClient(@Value("${oauth.kakao.client_id}") String clientId,
                            @Value("${oauth.redirect_url}") String redirectHost) {
        this.clientId = clientId;
        this.redirectUri = String.format("http://%s/oauth/kakao", redirectHost);
    }

    @Override
    public String postToken(String authCode) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("grant_type", "authorization_code");
        param.add("client_id", clientId);
        param.add("redirect_uri", redirectUri);
        param.add("code", authCode);

        ResponseEntity<KakaoTokenResponse> result = client.post()
                .uri(OAUTH_URI)
                .body(param)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new RestClientException("RestClient Exception: " + response.getStatusCode() + " " +  response.getBody());
                }))
                .toEntity(KakaoTokenResponse.class);

        if (result.getBody() == null) {
            throw new RestClientException("RestClient Exception: body is null");
        }

        return result.getBody().getAccessToken();
    }

    @Override
    public KakaoMeResponse getInfo(String accessToken) {
        ResponseEntity<KakaoMeResponse> result = client.get()
                .uri(API_URI + "/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new RestClientException("RestClient Exception: " + response.getStatusCode() + " " +  response.getBody());
                }))
                .toEntity(KakaoMeResponse.class);

        if (result.getBody() == null) {
            throw new RestClientException("RestClient Exception: body is null");
        }

        return result.getBody();
    }

}
