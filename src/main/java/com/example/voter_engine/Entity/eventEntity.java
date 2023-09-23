package com.example.voter_engine.Entity;

import com.example.voter_engine.validation.EventNo;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Component
@Data
@Entity
@Table(name = "EventEntity")
public class eventEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "registrationStartDate")
    private Date registrationStartDate;

    @Column(name = "registrationEndDate")
    private Date registrationEndDate;

    @Column(name = "electionStartDate")
    private Date electionStartDate;

    @Column(name = "electionEndDate")
    private Date electionEndDate;

    @EventNo(message = "your event status must be either 1 or 2 or 3 ")
    @Column(name = "status")
    private String status;

}
