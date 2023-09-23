package com.example.voter_engine.controller;

import com.example.voter_engine.Entity.*;
import com.example.voter_engine.Request.changePasswordRequest;
import com.example.voter_engine.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/admin")
public class adminController {
    
    @Autowired
    private com.example.voter_engine.service.adminService adminService;

    @DeleteMapping(value = "/deleteAllCandidate")
    public String deleteCandidate(@RequestBody candidate candidate){
        adminService.deleteCandidate(candidate);
        return "deleted";
    }

    @PostMapping(value = "/createVoter")
    public String createVoters(@RequestBody @Valid voterListPojo voterListPojo){
        return adminService.addVoter(voterListPojo);

    }

    @PostMapping(value = "/startElection")
    public String startElection(@RequestBody @Valid electionDate electionDate) throws ParseException {
        return adminService.startElection(electionDate);
    }

    @PostMapping(value = "/startRegistration")
    public String startRegistration(@RequestBody @Valid registrationDate registrationDate){
        return (adminService.startRegistration(registrationDate));
    }

    @GetMapping(value = "/releaseWinner")
    public  String releaseWinner(){
        return adminService.displayWinner();
    }

    @GetMapping(value = "/approval/{id}")
    public String approval(@PathVariable int[] id){
        return adminService.approval(id);
    }

    @GetMapping(value = "/deleteUserById/{id}")
    public String deleteUserById(@PathVariable int[] id){
        return adminService.DeleteUserById(id);
    }

    @GetMapping(value = "/deleteCandidateById/{id}")
    public String deleteCandidateById(@PathVariable int[] id){
        return adminService.DeleteCandidateById(id);
    }

    @GetMapping(value = "/deleteCandidateRequestById/{id}")
    public String deleteCandidateRequestById(@PathVariable int[] id){
        return adminService.DeleteCandidateRequestById(id);
    }

    @GetMapping(value = "/deleteVoterById/{id}")
    public String DeleteVoterById(@PathVariable int[] id){
        return adminService.DeleteVoterById(id);
    }


    @GetMapping(value = "/sendMailToUser")
    public String sendMailToUser(){
        return adminService.sendMailToUser();
    }

    @GetMapping(value = "/sendMailToUserById/{id}")
    public String sendMailToUserById(@PathVariable int[] id){
        return adminService.sendMailToUserById(id);
    }

    @GetMapping(value = "/sendMailToVoterById/{id}")
    public String sendMailToVoterById(@PathVariable int[] id){
        return adminService.sendMailToVoterById(id);
    }

    @GetMapping(value = "/sendMailToVoter")
    public String sendMailToVoter(){
        return adminService.sendMailToVoter();
    }


    @PostMapping(value = "/addUser")
    public String adduser(@RequestBody @Valid userPojo userPojo){
        return adminService.addUser(userPojo);
    }

    @GetMapping("/getVoterList")
    public Page<voterList> getVoterList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(defaultValue = "") String key) {

        return adminService.getVoterList(page,size,sort,key);
    }


    @GetMapping("/getCandidateList")
    public Page<candidateList> getCandidateList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(defaultValue = "") String key) {

        return adminService.getCandidateList(page,size,sort,key);
    }

    @GetMapping("/getCandidate")
    public Page<candidate> getCandidate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(defaultValue = "") String key) {

        return adminService.getCandidate(page,size,sort,key);
    }

    @GetMapping("/getUser")
    public Page<user> getUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(defaultValue = "") String key) {

        return adminService.getUser(page,size,sort,key);
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return adminService.uploadFile(file);
    }

    @GetMapping("/eventDetail")
    public Optional<eventEntity> eventDetail(){
        return adminService.EventDetail();
    }

    @GetMapping("/getUserById/{id}")
    public Optional<user> getUserById(@PathVariable int id){
        return adminService.getUserById(id);
    }

    @GetMapping("/getVoterById/{id}")
    public Optional<voterList> getVoterById(@PathVariable int id){
        return adminService.getVoterById(id);
    }

    @PostMapping("/editUser/{id}")
    public String EditUser(@PathVariable int id,@RequestBody user user) {
        return adminService.EditUser(id,user);
    }

    @PostMapping("/editVoter/{id}")
    public String EditVoter(@PathVariable int id,@RequestBody voterListPojo voterListPojo) {
        return adminService.EditVoter(id,voterListPojo);
    }

    @PostMapping("/editCandidate/{id}")
    public String EditCandidate(@PathVariable int id,@RequestBody candidate candidate) {
        return adminService.EditCandidate(id,candidate);
    }

    @PostMapping("/addVoter")
    public String addVoter(@RequestBody @Valid voterListPojo voterListPojo) {
        return adminService.addVoter(voterListPojo);
    }

    @GetMapping("/getCandidateListById/{id}")
    public Optional<candidateList> getCandidateListById(@PathVariable int id){
        return adminService.getCandidateListById(id);
    }

    @GetMapping("/getCandidateById/{id}")
    public Optional<candidate> getCandidateById(@PathVariable int id){
        return adminService.getCandidateById(id);
    }

    @PostMapping("/editCandidateList/{id}")
    public String EditCandidateList(@PathVariable int id,@RequestBody candidateList candidate){
        return adminService.EditCandidateList(id,candidate);
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody @Valid changePasswordRequest changePasswordRequest){
     return adminService.changePassword(changePasswordRequest);
    }

    @GetMapping("/deleteAdminProfileImage/{id}")
    public String deleteAdminProfileImage(@PathVariable int id){
        return adminService.deleteAdminProfileImage(id);
    }

}
