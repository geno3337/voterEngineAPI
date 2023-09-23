package com.example.voter_engine.pojo;

import com.example.voter_engine.Entity.candidateList;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@Component
public class candidatePojo {

    public candidatePojo(candidateList candidateList) {
        this.id=candidateList.getId();
        this.name=candidateList.getName();
        this.place=candidateList.getPlace();
        this.post=candidateList.getPost();
        this.gender=candidateList.getGender();
        this.detail=candidateList.getDetail();
        this.manifesto=candidateList.getManifesto();
        this.profileImage=candidateList.getProfileImage();
        this.gmail=candidateList.getGmail();
    }

    private int id;

    private String name;

    private String gmail;

    private String profileImage;

    private String place;

    private String post;

    private String gender;

    private int vote;

    private String detail;

    private String manifesto;

}
