package com.sum;

import com.sum.chatter.dto.auth.NaverMeResponse;
import com.sum.chatter.repository.entity.User;

public class TestFixture {

    public static User getUser() {
        return User.builder()
                .name("성준")
                .age("29")
                .address("jeju")
                .email("seongjki@gmail.com")
                .oauthId("13")
                .profileImg("13")
                .gender("man")
                .nickname("dd").build();
    }

    public static NaverMeResponse getNaverMeResponse() {
        return NaverMeResponse.builder()
                .oauthId("13")
                .name("sj")
                .gender("M")
                .age("30-39")
                .mobile("010-1234-5678")
                .build();
    }

}
