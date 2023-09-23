package com.example.voter_engine.pojo;

import com.example.voter_engine.validation.UserRole;
import com.example.voter_engine.validation.UserUniqueEmail;
import lombok.Data;
import org.springframework.stereotype.Indexed;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class userPojo {

    private int userId;

    @NotNull(message = "username cannot be null")
    private String userName;

//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$",
//            message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
//    @NotNull(message = "password cannot be null")
//    private String password;

    @UserUniqueEmail(message = " the email is already exist")
    @Email(message = "enter a correct email")
    @NotNull(message = "your email cannot be null")
    private String gmail;

    @UserRole(message = "some thing wrong with your role the role must be either user or admin")
//    @NotNull(message = "role cannot be null")
    private String role;

    private String about;

    private String Address;

    private String phone;

    private String gender;


    private String token;


    private LocalDateTime tokenCreationDate;


    private String profileImage;

}
