package com.example.voter_engine.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class EmailDetails {

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

}
