package com.example.voter_engine.service;

import com.example.voter_engine.Entity.*;
import com.example.voter_engine.Request.changePasswordRequest;
import com.example.voter_engine.Entity.expection.recordMismatchException;
import com.example.voter_engine.Entity.expection.resourceNotFound;
import com.example.voter_engine.pojo.*;
import com.example.voter_engine.repository.*;
import com.example.voter_engine.utility.ExcelUtil;
import com.example.voter_engine.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class adminService {
    
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private voterListRepository voterListRepository;

    @Autowired
    private candidateListRepository candidateListRepository;

    @Autowired
    private EmailQueueRepository emailQueueRepository;

    @Autowired
    private userRepository userRepository;

    @Autowired(required = true)
    private eventEntity eventEntity;

    @Autowired
    private EmailService emailService;

    @Autowired(required = true)
    private electionDate electionDate;

    @Autowired
    private registrationDate registrationDate;

    @Autowired
    private com.example.voter_engine.repository.eventEntityRepository eventEntityRepository;

    @Value("${spring.mail.username}")
    private String sender;

    public void deleteCandidate(candidate candidate) {
        candidateRepository.delete(candidate);
    }

    public String addVoter(voterListPojo voterListPojo) {

        voterList VL = new voterList();
//        VL.setVoter_id(voterListPojo.getVoter_id());
        VL.setVoter_age(voterListPojo.getVoter_age());
        VL.setVoter_name(voterListPojo.getVoter_name());
        VL.setGmail(voterListPojo.getGmail());
        voterListRepository.save(VL);
        return "success";

    }

    public String startElection(electionDate electionDate) {
        if(!electionDate.getElectionEndDate().after(electionDate.getElectionStartDate())){
            throw new RuntimeException( "endDate should be after startDate");
        }
        if (!eventEntityRepository.existsById(1)){
            throw new resourceNotFound("first start the registration phase");
        }
        Optional<eventEntity> eventEntity1=eventEntityRepository.findById(1);
        eventEntity eventEntity=eventEntity1.get();
        if (eventEntity.getStatus().equals("2")){
            throw  new RuntimeException( "election is already started");
        } else if (eventEntity.getStatus().equals("3")) {
            throw new RuntimeException( "winner is released so you cannot start the election");
        }
        if(!electionDate.getElectionStartDate().after(eventEntity.getRegistrationEndDate())){
            throw new RuntimeException( "election start date should be after registration endDate");
        }
        eventEntity.setElectionStartDate(electionDate.getElectionStartDate());
        eventEntity.setElectionEndDate(electionDate.getElectionEndDate());
        eventEntity.setStatus("2");
        eventEntityRepository.save(eventEntity);
        return "Election dates are saved";

    }

    public String startRegistration(registrationDate registrationDate) {
        if (eventEntityRepository.existsById(1)) {
            Optional<eventEntity> eventEntity1 = eventEntityRepository.findById(1);
            eventEntity eventEntity = eventEntity1.get();
            if((eventEntity.getStatus()).equals("1") ){
                throw new RuntimeException("registration is already started");
            }
            if (eventEntity.getStatus().equals("2") ) {
                throw new RuntimeException("election is started so you cannot start the registration");
            } else if (eventEntity.getStatus().equals("3")) {
                throw new RuntimeException("winner is released so you cannot start the registration");
            }
            if (!registrationDate.getRegistrationStartDate().before(registrationDate.getRegistrationEndDate())) {
                throw new RuntimeException("endDate should be after startDate");
            }

        }
        eventEntity.setId(1);
        eventEntity.setRegistrationStartDate(registrationDate.getRegistrationStartDate());
        eventEntity.setRegistrationEndDate(registrationDate.getRegistrationEndDate());
        eventEntity.setStatus("1");
        eventEntityRepository.save(eventEntity);
        return "registration started";
    }

    public String displayWinner() {
        if (!eventEntityRepository.existsById(1)){
            throw new resourceNotFound("first start the registration phase");
        }
        Optional<eventEntity> eventEntity1=eventEntityRepository.findById(1);
        eventEntity eventEntity=eventEntity1.get();
        if (eventEntity.getStatus().equals("1")){
            throw  new RuntimeException( "start the election");
        } else if (eventEntity.getStatus().equals("3")) {
            throw new RuntimeException( "the winner is already released");
        }
        eventEntity.setElectionStartDate(null);
        eventEntity.setElectionEndDate(null);
        eventEntity.setRegistrationStartDate(null);
        eventEntity.setRegistrationEndDate(null);
        eventEntity.setStatus("3");
        eventEntityRepository.save(eventEntity);
        return "success";
    }


    public String approval(int[] id) {
        for (int i = 0; i < id.length; i++) {
            if (candidateListRepository.existsById(id[i])==true){
                Optional<candidateList> candidate = candidateListRepository.findById(id[i]);
                candidateList candidateList =  candidate.get();
                if( candidateList.isEmailVerified()) {
                    candidateList.setAdminVerified(true);
                    candidateListRepository.save(candidateList);
                    candidatePojo candi = candidate.map(candidatePojo::new).get();
                    candidate ve = new candidate();
                    ve.setId(candi.getId());
                    ve.setName(candi.getName());
                    ve.setGmail(candi.getGmail());
                    ve.setPlace(candi.getPlace());
                    ve.setPost(candi.getPost());
                    ve.setGender(candi.getGender());
                    ve.setDetail(candi.getDetail());
                    ve.setManifesto(candi.getManifesto());
                    ve.setProfileImage(candi.getProfileImage());
                    candidateRepository.save(ve);
                }else {
                    throw new RuntimeException("this application is not email verified");
                }
        candidateListRepository.deleteById(id[i]);
            }
            else {
                throw new resourceNotFound("user not exist with id="+id[i]);
            }
        }
        return "success";
    }

    public String DeleteCandidateById(int[] id) {
        for (int i = 0; i < id.length; i++) {
            candidateRepository.deleteById(id[i]);
        }
        return ("deleted successfully");
    }

    public String DeleteUserById(int[] id) {
        for (int i = 0; i < id.length; i++) {
            userRepository.deleteById(id[i]);
           }
        return ("deleted successfully");
    }

    public String DeleteCandidateRequestById(int[] id) {
        for (int i = 0; i < id.length; i++) {
            candidateListRepository.deleteById(id[i]);
        }
        return ("deleted successfully");
    }

    public String DeleteVoterById(int[] id) {
        for (int i = 0; i < id.length; i++) {
            voterListRepository.deleteById(id[i]);
        }
        return ("deleted successfully");
    }


    public Page<user> getUser(int page,int size,String[] sort,String key) {

        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageRequest = PageRequest.of(page, size, sorting);
        if(key==null) {
            return userRepository.findAll(pageRequest);
        }
        return userRepository.search(key,pageRequest);
    }

    public String addUser(userPojo userPojo){
        user us = new user();
        us.setUserId(userPojo.getUserId());
        us.setUserName(userPojo.getUserName());
        us.setGmail(userPojo.getGmail());
//        us.setPassword(userPojo.getPassword());
        us.setPassword(Utility.generateSecurePassword());
        us.setRole(userPojo.getRole());
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost:8080").path("/common/getUserImage?id="+0+"&filename="+"default.jpg").build();
        us.setProfileImage(String.valueOf(uriComponents));
        userRepository.save(us);
        return "saved";
    }

    public String sendMailToUser() {
        List<user> user1 = userRepository.findAll();
        for (int i = 0; i < user1.size(); i++) {
//            EmailDetails ED=new EmailDetails();
//            ED.setSubject( "invitetation for election");
//            ED.setRecipient(user1.get(i).getGmail());
//            ED.setMsgBody("UserName:"+user1.get(i).getUserName()+"  "+"password:"+user1.get(i).getPassword());
//            emailService.sendSimpleMail(ED);
            emailQueue EQ=new emailQueue();
            EQ.setSubject( "invitetation for election");
            EQ.setToEmails(user1.get(i).getGmail());
            EQ.setMessage("UserName:"+user1.get(i).getUserName()+"  "+"password:"+user1.get(i).getPassword());
            EQ.setStatus("pending");
            EQ.setFromEmail(sender);
            EQ.setNoofAttempts(0);
            emailQueueRepository.save(EQ);
        }
        return "success";
    }

    public String sendMailToUserById(int[] id){
        for (int i = 0; i < id.length; i++) {
            Optional<user> user = userRepository.findById(id[i]);
            user user1=user.get();
                emailQueue EQ=new emailQueue();
                EQ.setSubject( "invitetation for election");
                EQ.setToEmails(user1.getGmail());
                EQ.setMessage("UserName:"+user1.getUserName()+"  "+"password:"+user1.getPassword());
                EQ.setStatus("pending");
                EQ.setFromEmail(sender);
                EQ.setNoofAttempts(0);
                emailQueueRepository.save(EQ);
        }
        return "success";
    }

    public String sendMailToVoter(){
        List<voterList> user1 = voterListRepository.findAll();
        for (int i = 0; i < user1.size(); i++) {
            emailQueue EQ=new emailQueue();
            EQ.setSubject( "to release the voter id");
            EQ.setToEmails(user1.get(i).getGmail());
            EQ.setMessage("UserId:"+user1.get(i).voter_id);
            EQ.setStatus("pending");
            EQ.setFromEmail(sender);
            EQ.setNoofAttempts(0);
            emailQueueRepository.save(EQ);
        }
        return "success";
    }

    public String sendMailToVoterById(int[] id){
        for (int i = 0; i < id.length; i++) {
            Optional<voterList> voter = voterListRepository.findById(id[i]);
            voterList voter1=voter.get();
                emailQueue EQ=new emailQueue();
                EQ.setSubject( "to release the voter id");
                EQ.setToEmails(voter1.getGmail());
                EQ.setMessage("UserId:"+voter1.voter_id);
                EQ.setStatus("pending");
                EQ.setFromEmail(sender);
                EQ.setNoofAttempts(0);
                emailQueueRepository.save(EQ);
        }
        return "success";
    }

//    public List<voterList> getVoterList(){
//        return voterListRepository.findAll();
//    }

    public Page<voterList> getVoterList(int page,int size,String[] sort,String key) {

        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageRequest = PageRequest.of(page, size, sorting);
        if(key==null) {
            return voterListRepository.findAll(pageRequest);
        }
        return voterListRepository.search(key,pageRequest);
    }

//
//    public Page<candidateList> getcandidateList(int pageNo,int size){
//        Pageable Page = PageRequest.of(pageNo, size);
//        return candidateListRepository.findAllByIsAdminVerified(false,Page);
//    }

    public Page<candidate> getCandidate(int page,int size,String[] sort,String key) {

        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageRequest = PageRequest.of(page, size, sorting);
        if(key==null) {
            return candidateRepository.findAll(pageRequest);
        }
        return candidateRepository.search(key,pageRequest);
    }

    public Page<candidateList> getCandidateList(int page,int size,String[] sort,String key) {

        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageRequest = PageRequest.of(page, size, sorting);
        if(key==null) {
            return candidateListRepository.findAll(pageRequest);
        }
        return candidateListRepository.search(key,pageRequest);
    }
    public ResponseEntity<String> uploadFile( MultipartFile file) {
        try {
            List<user> user1 = ExcelUtil.parseExcel(file.getInputStream());
            userRepository.saveAll(user1);
            return ResponseEntity.ok("Data uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload data.");
        }
    }

    public Optional<com.example.voter_engine.Entity.eventEntity> EventDetail(){
        return eventEntityRepository.findById(1);
    }

    public Optional<user> getUserById(int id){
        return userRepository.findById(id);
    }

    public Optional<voterList> getVoterById(int id){
        return voterListRepository.findById(id);
    }

    public Optional<candidate> getCandidateById(int id){
        return candidateRepository.findById(id);
    }

    public Optional<candidateList> getCandidateListById(int id){
        return candidateListRepository.findById(id);
    }

    public String EditUser(int id,user user){
        Optional<com.example.voter_engine.Entity.user> user1=userRepository.findById(id);
        user user2=user1.get();
        if(!(user2.getGmail()).equals(user.getGmail())){
            if(userRepository.existsByGmail(user.getGmail())){
                throw  new RuntimeException( "Email already Exist");
            }
            user2.setGmail(user.getGmail());
        }
        user2.setUserName(user.getUserName());
        user2.setRole(user.getRole());
        userRepository.save(user2);
        return "saved";
    }

    public String EditVoter(int id,voterListPojo voterList){
        Optional<com.example.voter_engine.Entity.voterList> voter1=voterListRepository.findById(id);
        voterList voter2=voter1.get();
        if(!(voter2.getGmail()).equals(voterList.getGmail())){
            if(voterListRepository.existsByGmail(voterList.getGmail())){
                throw  new RuntimeException( "Email already Exist");
            }
            voter2.setGmail(voterList.getGmail());
        }
        voter2.setVoter_name(voterList.getVoter_name());
        voter2.setVoter_age(voterList.getVoter_age());
        voterListRepository.save(voter2);
        return "saved successfully";
    }

    public String EditCandidate(int id,candidate candidate){
        Optional<com.example.voter_engine.Entity.candidate> candidate1=candidateRepository.findById(id);
        candidate candidate2=candidate1.get();
        if(!(candidate2.getGmail()).equals(candidate.getGmail())){
            if(candidateRepository.existsByGmail(candidate.getGmail())){
                throw  new RuntimeException( "Email already Exist");
            }
            candidate2.setGmail(candidate.getGmail());
        }
        candidate2.setName(candidate.getName());
        candidate2.setGender(candidate.getGender());
        candidate2.setPlace(candidate.getPlace());
        candidateRepository.save(candidate2);
        return "saved";
    }
    public String EditCandidateList(int id,candidateList candidate){
        Optional<candidateList> candidate1=candidateListRepository.findById(id);
        candidateList candidate2=candidate1.get();
        if(!candidate2.getGmail().equals(candidate.getGmail())){
            if(candidateRepository.existsByGmail(candidate.getGmail())){
                throw  new RuntimeException( "Email already Exist");
            }
            candidate2.setGmail(candidate.getGmail());
        }
        candidate2.setName(candidate.getName());
        candidate2.setGender(candidate.getGender());
        candidate2.setPlace(candidate.getPlace());
        candidateListRepository.save(candidate2);
        return "saved";
    }

    public String changePassword(changePasswordRequest changePasswordRequest,String gmail) {

        if (!userRepository.existsByGmail(gmail)){
            throw new resourceNotFound( "invalid user");
        }

        Optional<user> userOptional = Optional.ofNullable(userRepository.findByGmail(gmail));

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConformPassword())){
            throw  new recordMismatchException( "password and conformPassword are not same");
        }

        user user = userOptional.get();

        user.setPassword(changePasswordRequest.getNewPassword());

        userRepository.save(user);

        return "Your password successfully updated.";
    }

    public String deleteAdminProfileImage(int id){
        Optional<user> userOptional= userRepository.findById(id);
        user us=userOptional.get();
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost:8080").path("/common/getUserImage?id="+0+"&filename="+"default.jpg").build();
//        us.setProfileImage(String.valueOf(uriComponents));
        us.setProfileImage(null);
        userRepository.save(us);
        return "image deleted successfully";
    }

}
