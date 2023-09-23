package com.example.voter_engine.Request;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Data
@Component
public class changePasswordRequest {

    @NotNull(message = "currentPassword should not be null")
    private String currentPassword;

    @NotNull(message = "NewPassword should not be null")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$", message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String NewPassword;

    @NotNull(message = "conformPassword should not be null")
    private String conformPassword;
}
