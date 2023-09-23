package com.example.voter_engine.Entity;



import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name ="candidates" ,schema="voter_engine")
public class candidate {

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

    @Column(name = "profileImage")
    private String profileImage;

    @Column(name = "vote")
    private int vote;


}




