package com.sum.chatter.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.sum.TestFixture;
import com.sum.chatter.repository.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByOauthId() {
        //given
        User user = TestFixture.getUser();
        User expected = userRepository.save(user);

        //when
        User result = userRepository.findByOauthId(user.getOauthId()).get();

        //then
        assertEquals(expected, result);
    }

}
