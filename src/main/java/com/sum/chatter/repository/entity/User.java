package com.sum.chatter.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = {@Index(name = "idx_nickname", columnList = "nickname")})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String oauthId;

    @NotNull
    @Size(min = 2, max = 18)
    private String name;

    @NotNull
    private String gender;

    @NotNull
    private String age;

    @NotNull
    private String address;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 2, max = 30)
    private String nickname;

    @NotNull
    private String profileImg;

}
