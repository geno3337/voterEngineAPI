package com.example.voter_engine.Entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId")
    private int userId;

    @NotNull
    @Column(name="userName")
    private String userName;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "gmail")
    private String gmail;

    @NotNull
    @Column(name = "role")
    private String role;

    @Column(name = "about")
    private String about;

    @Column(name = "Address")
    private String Address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "gender")
    private String gender;

    @Column(name = "passwordResetToken")
    private String passwordResetToken;

    @Column(name = "PswtokenCreationDate")
    private LocalDateTime pswtokenCreationDate;

    @Column(name = "profileImage")
    private String profileImage;


}
