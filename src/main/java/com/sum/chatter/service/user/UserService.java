package com.sum.chatter.service.user;

import com.sum.chatter.dto.UserDto;
import com.sum.chatter.dto.UserSignUpDto;
import com.sum.chatter.repository.UserRepository;
import com.sum.chatter.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserDto signUp(UserSignUpDto signUpDto) {
        if (userRepository.findByOauthId(signUpDto.getOauthId()).isPresent()) {
            throw new IllegalArgumentException("Already SignUp User");
        }

        User user = userRepository.save(modelMapper.map(signUpDto, User.class));

        return modelMapper.map(user, UserDto.class);
    }

}
