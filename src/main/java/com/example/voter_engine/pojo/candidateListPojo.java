package com.example.voter_engine.pojo;

import com.example.voter_engine.validation.CandidateUniqueEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class candidateListPojo {

    private int id;

    @NotNull(message = "name field cannot be null")
    private String name;

    @NotNull(message = "place field cannot be null")
    private String place;

    @NotNull(message = "post field cannot be null")
    private String post;

    @NotNull(message = "gender field cannot be null")
    private String gender;


    @CandidateUniqueEmail( message = "email is already exist")
    @NotNull(message = "email field cannot be null")
    @Email(message = "enter a correct email")
    private String gmail;

    @Size(max=100 ,message="details must be  atleast 100 words  ")
    @NotNull(message = "detail field cannot be null")
    private String detail;

//    @Size(min = 100,max = 500,message = "manifesto should be of minimum length 100 and maximum length 500")
    @NotNull(message = "manifesto field cannot be null")
    private String manifesto;

//    @NotNull(message = "profileImage field cannot be null")
    private MultipartFile profileImage;

    private boolean isEmailVerified;

    private boolean AdminVerified;

    private String emailVerificationToken;

    private LocalDateTime tokenCreationDate;

}
