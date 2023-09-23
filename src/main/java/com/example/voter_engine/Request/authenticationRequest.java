package com.example.voter_engine.Request;

import lombok.Data;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Component
public class authenticationRequest {

    @NotNull(message = "username should not be null")
    private String email;

    @NotNull(message = "password should not be null")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$", message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String password;


}
