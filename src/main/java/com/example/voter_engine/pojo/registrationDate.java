package com.example.voter_engine.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Future;
import java.util.Date;

@Component
@Data
public class registrationDate {

//    @Future(message = "start date must be after current date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date registrationStartDate;

//    @EndDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date registrationEndDate;
}
