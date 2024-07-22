package com.sum.chatter;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class RestDocFieldDescriptors {

    public static List<FieldDescriptor> getSignUpDescriptor() {
        return List.of(
                fieldWithPath("oauthId").type(JsonFieldType.STRING).description("Oauth 서버 아이디"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                fieldWithPath("age").type(JsonFieldType.STRING).description("나이"),
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("profileImg").type(JsonFieldType.STRING).description("프로필 이미지 주소")
        );
    }

    public static List<FieldDescriptor> getUserDescriptor() {
        return List.of(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("서버 아이디"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                fieldWithPath("age").type(JsonFieldType.STRING).description("나이"),
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("profileImg").type(JsonFieldType.STRING).description("프로필 이미지 주소")
        );
    }

}
