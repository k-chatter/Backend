package com.sum.chatter.repository;

import com.sum.chatter.repository.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthId(String oauthId);

}
