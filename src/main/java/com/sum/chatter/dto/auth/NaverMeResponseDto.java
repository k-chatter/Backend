package com.sum.chatter.dto.auth;

import lombok.Data;

@Data
public class NaverMeResponseDto {

    private String resultCode;

    private String message;

    private NaverMeResponse response;

}
