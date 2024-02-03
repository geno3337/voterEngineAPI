package com.example.voter_engine.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name ="candidatesList" ,schema="voter_engine")
@Component
public class candidateList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @Column(name="name" ,nullable = false)
    private String name;

    @Column(name="place",nullable = false)
    private String place;

    @Column(name="post",nullable = false)
    private String post;

    @Column(name="gender",nullable = false)
    private String gender;

    @Column(name="gmail" )
    private String gmail;

    @Column(name = "detail", length = 2000,nullable = false)
    private String detail;

    @Column(name = "manifesto",columnDefinition = "TEXT" , nullable = false)
    private String manifesto;

    @Column(name = "isEmailVerified")
    private boolean isEmailVerified;

    @Column(name = "AdminVerified")
    private boolean AdminVerified;

    @Column(name = "emailVerificationToken")
    private String emailVerificationToken;

    @Column(name = "tokenCreationDate")
    private LocalDateTime tokenCreationDate;

    @Column(name = "profileImage")
    private String profileImage;

}
