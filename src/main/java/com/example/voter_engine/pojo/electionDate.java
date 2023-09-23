package com.example.voter_engine.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class electionDate {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date electionStartDate;

//    @ElectionEndDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date electionEndDate;
    
}
