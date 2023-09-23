package com.example.voter_engine.controller;

import com.example.voter_engine.Entity.candidate;
import com.example.voter_engine.Entity.emailQueue;
import com.example.voter_engine.Request.authenticationRequest;
import com.example.voter_engine.Request.resetPasswordRequest;
import com.example.voter_engine.Response.jwtResponse;
import com.example.voter_engine.repository.EmailQueueRepository;
import com.example.voter_engine.scheduler.EmailScheduler;
import com.example.voter_engine.service.EmailService;
import com.example.voter_engine.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(value = "/common")
public class commonController {

    @Autowired
    private com.example.voter_engine.service.commonService commonService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailQueueRepository emailQueueRepository;

    @Autowired
    private EmailScheduler emailScheduler;

    @Autowired
    private userService userService;

    @GetMapping(value = "/viewCandidate/{pageNo}")
    public Page<candidate> view(@PathVariable int pageNo){
        Pageable Page = PageRequest.of(pageNo, 5);
        return commonService.getAll(Page);
    }

    @PostMapping(value = "/authentication")
    public ResponseEntity<jwtResponse> createtoken(@RequestBody authenticationRequest authenticationRequest){
        return commonService.createToken(authenticationRequest);
    }

//    @GetMapping(value = "/searchCandidate/{keyword}")
//    public List<candidate> searchCandidate(@PathVariable  String keyword){
//        return commonService.searchCandidate(keyword);
//    }

    @PostMapping(value = "/forgetPassword/{gmail}")
    public String ForgetPassword(@PathVariable @Valid @Email(message = "enter a valid email") String gmail){
        return commonService.forgotPassword(gmail);
    }

    @PostMapping(value = "/resetPassword")
    public String ResetPassword(@RequestBody @Valid resetPasswordRequest resetPasswordRequest, HttpServletRequest request){
        return commonService.resetPassword(resetPasswordRequest,request);
    }

    @GetMapping(value = "/passwordResetTokenVerification")
    public RedirectView passwordResetTokenVerification(@RequestParam("token") String token){
        return commonService.passwordResetTokenVerification(token);
    }

    @GetMapping(value = "/resendPasswordToken")
    public String resendPasswordToken(@PathVariable @Valid @Email(message = "enter a valid email") String gmail){
        return commonService.resendPasswordToken(gmail);
    }

    @GetMapping("/getUserImage")
    public UrlResource getUserImage(@RequestParam("id") String id, @RequestParam("filename") String filename){
        return commonService.getImage("user/"+id,filename);
    }


    @GetMapping("/getCandidateImage")
    public UrlResource getCandidateImage(@RequestParam("id") String id, @RequestParam("filename") String filename){
        return commonService.getImage("candidate/"+id,filename);
    }

    @GetMapping(value = "/emailTokenVerification")
    public RedirectView emailTokenVerification(@RequestParam("token") String token){
        return userService.emailTokenVerification(token);
    }

//    @GetMapping(value ="/mail" )
//    public String mail(@RequestBody emailQueue email) {
////        return email;
//        try {
//            emailScheduler.sendEmail(email);
//            return "Email sent successfully";
//        } catch (Exception e) {
//            // Handle the exception gracefully, e.g., log it and return an error message
//            return "Failed to send email: " + e.getMessage();
//        }
//    }



//    @GetMapping("/getCandidate/{pageNo}")
//    public Page<candidateList> getCandidate(@PathVariable int pageNo,@RequestParam("size") int size){
//        return commonService.getcandidate(pageNo,size);
//    }





}
