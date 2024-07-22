package com.sum.chatter.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sum.TestFixture;
import com.sum.chatter.dto.UserDto;
import com.sum.chatter.dto.UserSignUpDto;
import com.sum.chatter.repository.UserRepository;
import com.sum.chatter.service.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Transactional
public class UserServiceTest {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
        this.userService = new UserService(userRepository, modelMapper);
    }

    @Test
    void 유저_회원가입_성공() {
        //given
        UserSignUpDto userSignUpDto = UserSignUpDto.builder()
                .name("성준")
                .age("29")
                .address("jeju")
                .email("seongjki@gmail.com")
                .profileImg("13")
                .gender("man")
                .nickname("dd")
                .oauthId("13")
                .build();

        //when
        UserDto result = userService.signUp(userSignUpDto);

        //then
        assertEquals(UserDto.builder()
                .id(1L)
                .name("성준")
                .age("29")
                .address("jeju")
                .email("seongjki@gmail.com")
                .profileImg("13")
                .gender("man")
                .nickname("dd")
                .build(), result);
    }

    @Test
    void 유저_회원가입_실패_잘못된_데이터() {
        //given
        UserSignUpDto userSignUpDto = UserSignUpDto.builder()
                .name("성준")
                .age("29")
                .address("jeju")
                .email("seongjki@gmail.com")
                .profileImg("13")
                .gender("man")
                .nickname("dd")
                .build();

        //when, then
        assertThrows(ConstraintViolationException.class, () -> userService.signUp(userSignUpDto));
    }

    @Test
    void 유저_회원가입_실패_존재하는_유저() {
        //given
        userRepository.save(TestFixture.getUser());
        UserSignUpDto userSignUpDto = UserSignUpDto.builder()
                .name("성준")
                .age("29")
                .address("jeju")
                .email("seongjki@gmail.com")
                .profileImg("13")
                .gender("man")
                .nickname("dd")
                .oauthId("13")
                .build();

        //when, then
        assertThrows(IllegalArgumentException.class, () -> userService.signUp(userSignUpDto));
    }

}
