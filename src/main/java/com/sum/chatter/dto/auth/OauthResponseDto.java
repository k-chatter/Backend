package com.sum.chatter.dto.auth;

import com.sum.chatter.dto.UserSignUpDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OauthResponseDto {

    private int statusCode;

    private UserSignUpDto user;

    private String token;

}
