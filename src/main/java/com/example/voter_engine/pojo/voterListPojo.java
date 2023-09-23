package com.example.voter_engine.pojo;

import com.example.voter_engine.validation.VoterUniqueEmail;
import jdk.jfr.BooleanFlag;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class voterListPojo {

    public int voter_id;


    @NotNull(message = "voter name should not be empty")
    private String voter_name;

    @NotNull(message = "voter age should not be null")
    @Min(value = 18 ,message = "voter age must be greater then 18")
    @Max(value = 70,message = "your age must be less then 70")
    public int voter_age;

    @NotNull
    @Email
    @VoterUniqueEmail(message = "email is already exist")
    public String gmail;


    public int isVoted;

}
