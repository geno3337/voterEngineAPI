package com.example.voter_engine.controller;

import com.example.voter_engine.Entity.candidate;
import com.example.voter_engine.Entity.user;
import com.example.voter_engine.pojo.candidateListPojo;
import com.example.voter_engine.pojo.userPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping(value="/user")
public class userController {
    
    @Autowired
    private com.example.voter_engine.service.userService userService;

    @Autowired
    private com.example.voter_engine.repository.candidateListRepository candidateListRepository;

    @GetMapping(value ="/maxVote")
    public int maxVote(){
        return userService.maxVote();
    }

    @GetMapping(value = "/addVote/{id}")
    public String addVote(@PathVariable("id") int id, @RequestParam("voter_id") int voter_id) throws ParseException {
        return userService.addVote(id,voter_id);
    }

    @GetMapping("/releaseWinner")
    public Page<candidate> releaseWinner(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "vote,asc") String[] sort,
            @RequestParam(defaultValue = "") String key) {

        return userService.releaseWinner(page,size,sort,key);
    }
//
//    @GetMapping(value = "/whichevent")
//    public String whichEvent(){
//        return(userService.whichEvent());
//    }

    @PostMapping(value = "/cadidateApply")
    public String candidateApply(@ModelAttribute @Valid candidateListPojo candidateListPojo , BindingResult result) throws IOException, MethodArgumentNotValidException {
        return userService.candidateApplication(candidateListPojo,result);
    }

//    @GetMapping(value = "/emailTokenVerification")
//    public RedirectView emailTokenVerification(@RequestParam("token") String token){
//        return userService.emailTokenVerification(token);
//    }

    @GetMapping(value = "/resendEmailVerification/{gmail}")
    public void resendEmailVerification(@PathVariable @Valid @Email String email){
        userService.resendEmailVerification(email);
    }

    @PostMapping(value = "/uploadCandidateImage/{id}")
    public String uploadCandidateImage(@PathVariable("id")  String id,@RequestParam("image") MultipartFile multipartFile) throws IOException {
        return userService.uploadCandidateProfile(id,multipartFile);
    }

    @GetMapping("/getCandidate")
    public Page<candidate> getCandidate(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "") String key){
        return userService.getCandidate(page,size,key);
    }

    @PostMapping("/uploadUserImage/{id}")
    public  String uploadUserImage(@PathVariable int id, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        return userService.uploadUserImage(id,multipartFile);
    }

    @GetMapping("/deleteUserImage/{id}")
    public  String deleteUserImage(@PathVariable int id) throws IOException {
        return userService.deleteUserImage(id);
    }


    @GetMapping("/getUser")
    public Page<user> getUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(defaultValue = "") String key) {

        return userService.getUser(page,size,sort,key);
    }

    @GetMapping("/eventDetail")
    public Optional<com.example.voter_engine.Entity.eventEntity> eventDetail(){
        return userService.EventDetail();
    }

    @GetMapping("/getUserByEmail/{gmail}")
    public user getUserByEmail(@PathVariable String gmail){
        return userService.getUserByEmail(gmail);
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@PathVariable int id,@RequestBody userPojo userPojo){
        return userService.editUser(id,userPojo);
    }
}
