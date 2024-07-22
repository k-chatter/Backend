package com.sum.chatter.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sum.chatter.RestDocFieldDescriptors;
import com.sum.chatter.dto.UserDto;
import com.sum.chatter.dto.UserSignUpDto;
import com.sum.chatter.service.auth.JwtBuilder;
import com.sum.chatter.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtBuilder jwtBuilder;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void 회원가입() throws Exception {
        UserSignUpDto userSignUpDto = UserSignUpDto.builder()
                .oauthId("13")
                .name("sj")
                .age("19")
                .address("jeju")
                .email("seongjki@gmail.com")
                .profileImg("14")
                .nickname("sj")
                .gender("man")
                .build();

        UserDto result = UserDto.builder()
                .id(1L)
                .name("sj")
                .age("19")
                .address("jeju")
                .email("seongjki@gmail.com")
                .profileImg("14")
                .nickname("sj")
                .gender("man")
                .build();

        Mockito.doReturn(result).when(userService).signUp(userSignUpDto);


        mockMvc.perform(RestDocumentationRequestBuilders.post("http://localhost/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignUpDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(signUpResultHandler())
                .andDo(print());

        Mockito.verify(userService).signUp(userSignUpDto);
    }

    private RestDocumentationResultHandler signUpResultHandler() {
        return document("users/post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(RestDocFieldDescriptors.getSignUpDescriptor()),
                responseFields(RestDocFieldDescriptors.getUserDescriptor())
        );
    }

}

