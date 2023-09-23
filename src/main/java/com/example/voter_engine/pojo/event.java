//package com.example.voter_engine.pojo;
//
//import com.example.voter_engine.validation.ElectionEndDate;
//import com.example.voter_engine.validation.ElectionStartDate;
//import com.example.voter_engine.validation.EndDate;
//import com.example.voter_engine.validation.EventNo;
//import lombok.Data;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.stereotype.Component;
//
//import javax.validation.constraints.Future;
//import java.util.Date;
//
//@Data
//@Component
//public class event {
//
//
//    private int id;
//
//    @Future(message = "start date must be after current date")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private Date registrationStartDate;
//
//    @EndDate
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private Date registrationEndDate;
//
//    @ElectionStartDate
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private Date electionStartDate;
//
//    @ElectionEndDate
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private Date electionEndDate;
//
//    @EventNo
//    private String Status;
//
//}
