package com.sum.chatter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpDto {

    private String oauthId;

    private String name;

    private String gender;

    private String age;

    private String address;

    private String email;

    private String nickname;

    private String profileImg;

}
