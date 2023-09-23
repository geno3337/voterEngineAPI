package com.example.voter_engine.Entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Component
@Data
@Entity
@Table(name = "emailQueue" ,schema="voter_engine")
public class emailQueue {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id" )
    private int id;

    @Column(name = "subject")
    private String subject;


    @Column(name = "from_email")
    private String fromEmail;

    @Column(name = "to_emails")
    private String toEmails;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private String status;

    @Column(name = "no_of_attempts")
    private Integer noofAttempts;

    @Column(name = "queued_at")
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date queuedAt;

    @Column(name = "processed_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date processedAt;

    @Column(name = "description")
    private String description;

}
