package com.example.voter_engine.Entity;

import jdk.jfr.BooleanFlag;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "voters")
public class voterList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "voter_id")
    public int voter_id;

    @NotNull
    @Column(name = "voter_name")
    private String voter_name;

    @Column(name = "voter_age")
    public int voter_age;

    @Column(name="gmail")
    public String gmail;


    @Column(name = "isVoted")
    public int isVoted;


}
