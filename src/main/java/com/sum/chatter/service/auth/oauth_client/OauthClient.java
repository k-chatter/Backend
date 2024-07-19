package com.sum.chatter.service.auth.oauth_client;

import com.sum.chatter.dto.auth.MeResponse;

public interface OauthClient {

    String postToken(String authCode);

    MeResponse getInfo(String accessToken);

}