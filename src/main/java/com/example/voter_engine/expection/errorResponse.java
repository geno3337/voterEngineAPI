package com.example.voter_engine.expection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class errorResponse {
    String message;
    int statsCode;

}
