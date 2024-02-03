package com.example.voter_engine.service;


import com.example.voter_engine.Entity.*;
import com.example.voter_engine.Entity.expection.resourceNotFound;
import com.example.voter_engine.pojo.EmailDetails;
import com.example.voter_engine.pojo.candidateListPojo;
import com.example.voter_engine.pojo.userPojo;
import com.example.voter_engine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class userService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private voterListRepository voterListRepository;

    @Autowired
    private candidateListRepository candidateListRepository;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private com.example.voter_engine.utility.resetPasswordTokenUtil resetPasswordTokenUtil;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String sender;

//    @Autowired
//    private com.example.voter_engine.Entity.eventEntity eventEntity;

    @Autowired
    private com.example.voter_engine.repository.eventEntityRepository eventEntityRepository;

    @Autowired
    private ImageStorageServiceImpl imageStorageService;

    @Autowired
    private EmailQueueRepository emailQueueRepository;


    public String addVote(int id, int voter_id) throws ParseException {
        if (!eventEntityRepository.existsById(1)){
            throw new resourceNotFound("first start the registration phase");
        }
        Optional<eventEntity> eventEntity1=eventEntityRepository.findById(1);
        eventEntity eventEntity=eventEntity1.get();
        if (eventEntity.getStatus().equals("1")){
            throw  new RuntimeException( "election is not yet started");
        } else if (eventEntity.getStatus().equals("3")) {
            throw new RuntimeException( "voting is ended");
        }
//        if (eventEntity.getElectionStartDate() == null && eventEntity.getElectionEndDate() == null) {
////            return "election is not yet started";
//            throw new resourceNotFound("election is not yet started");
//        }
        Date currentDate = new Date();
        Date startDate = eventEntity.getElectionStartDate();
        Date endDate = eventEntity.getElectionEndDate();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Date sd = sdf.parse(startDate);
//        Date ed = sdf.parse(endDate);
        if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0) {
//        if (startDate.after(currentDate) && endDate.before(currentDate)){
            if(!voterListRepository.existsById(voter_id)){
                throw new resourceNotFound("your voter id is incorrect");
            }
            if (check(voter_id) == true) {
                if (voterListRepository.findIsVoteById(voter_id) == 0) {
                    int vote = candidateRepository.findVoteById(id) + 1;
                    candidateRepository.updateVote(vote, id);
                    voterListRepository.updateIsVoted(voter_id);
                    return "vote were added successfully";
                } else {
//                    return "your vote were already saved";
                    throw new resourceNotFound("your vote were already saved");
                }
            }
//            return "you are not eligible for voting";
            throw new resourceNotFound("you are not eligible for voting");
        } else {
//            return "voting is ended";
            throw new resourceNotFound("current date is not within election date");
        }
    }

    public int maxVote() {
        return candidateRepository.max();
    }

    public Page<candidate> releaseWinner(int page, int size, String[] sort, String key){
        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageRequest = PageRequest.of(page, size, sorting);
        if(key==null) {
            return candidateRepository.findAll(pageRequest);
        }
        return candidateRepository.searchWinner(key,pageRequest);
    }

    public Page<voterList> getVoterList(int page, int size, String[] sort, String key) {

        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageRequest = PageRequest.of(page, size, sorting);
        if(key==null) {
            return voterListRepository.findAll(pageRequest);
        }
        return voterListRepository.search(key,pageRequest);
    }

    public String candidateApplication(candidateListPojo clp, BindingResult result) throws MethodArgumentNotValidException, IOException {


        if(result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Optional<eventEntity> eventEntity1=eventEntityRepository.findById(1);
        eventEntity eventEntity=eventEntity1.get();
        if (eventEntity.getStatus().equals("2")){
            throw  new RuntimeException( "registration is ended");
        } else if (eventEntity.getStatus().equals("3")) {
            throw new RuntimeException( "registration is ended");
        }
//        if(!eventEntityRepository.existsById(1)){
//            throw new RuntimeException("registration is not yet started");
//        }
//        Optional<com.example.voter_engine.Entity.eventEntity> eventEntity1=eventEntityRepository.findById(1);
//        eventEntity eventEntity2=eventEntity1.get();
//        Date currentDate = new Date();
//        Date startDate = eventEntity2.getElectionStartDate();
//        Date endDate = eventEntity2.getElectionEndDate();
//        if (startDate == null && endDate == null) {
//            throw new RuntimeException("registration is not yet started");
//        }
//        if(startDate.after(currentDate) && endDate.before(endDate)){
//            throw new RuntimeException("registration is not yet started");
//        }
        if(!eventEntity.getStatus().equals("1"))
        {
            throw  new RuntimeException( "registration is not yet started");
        }
        candidateList cl=new candidateList();
//        cl.setId(clp.getId());
        cl.setName(clp.getName());
        cl.setGender(clp.getGender());
        cl.setDetail(clp.getDetail());
        cl.setPlace(clp.getPlace());
        cl.setPost(clp.getPost());
        cl.setGmail(clp.getGmail());
        cl.setManifesto(clp.getManifesto());
        if(clp.getProfileImage()==null) {
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .scheme("http").host("localhost:8080").path("/common/getCandidateImage?id=" + 0 + "&filename=" + "default.jpg").build();
            cl.setProfileImage(String.valueOf(uriComponents));
        }else{
            cl.setProfileImage(uploadCandidateProfile(clp.getGmail(),clp.getProfileImage()));
        }
        String token=resetPasswordTokenUtil.generateToken();
        cl.setEmailVerificationToken(token);
        cl.setTokenCreationDate(LocalDateTime.now());
        String link = "http://localhost:8080/common/emailTokenVerification?token=" +token;
        String subject = "Here's the link to verify your application";
        String content ="<p>Click the link below to verify your application:</p>"
                        + "<p><a href=\"" + link + "\">Verify</a></p>";
        emailQueue EQ=new emailQueue();
        EQ.setSubject( subject);
        EQ.setToEmails(clp.getGmail());
        EQ.setMessage(content);
        EQ.setStatus("pending");
        EQ.setFromEmail(sender);
        EQ.setNoofAttempts(0);
        emailQueueRepository.save(EQ);
        candidateListRepository.save(cl);
        return "success";

    }

    public String uploadCandidateProfile(String id,MultipartFile multipartFile) throws IOException {
//        if(!eventEntityRepository.existsById(1)){
//            throw new RuntimeException("registration is not yet started");
//        }
//        Optional<com.example.voter_engine.Entity.eventEntity> eventEntity1=eventEntityRepository.findById(1);
//        eventEntity eventEntity2=eventEntity1.get();
//        Date currentDate = new Date();
//        Date startDate = eventEntity2.getRegistrationStartDate();
//        Date endDate = eventEntity2.getRegistrationEndDate();
//        if (startDate == null && endDate == null) {
//            throw new RuntimeException("registration is not yet started");
//        }
//        if(startDate.after(currentDate) && endDate.before(endDate)){
//            throw new RuntimeException("registration is not yet started");
//        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//        Optional<candidateList> candidateOptional= candidateListRepository.findByGmail(id);
//        candidateList candi=candidateOptional.get();
        imageStorageService.saveImage("candidate/"+String.valueOf(id),multipartFile);
//        candi.setProfileImage(imageStorageService.getCandidateImageUrl(String.valueOf(id),fileName));
//        candidateListRepository.save(candi);
        return imageStorageService.getCandidateImageUrl(String.valueOf(id),fileName);

    }

    public void candidateEmailVerification(String gmail){

        String token=resetPasswordTokenUtil.generateToken();
        Optional<candidateList> candi=candidateListRepository.findByGmail(gmail);
        candidateList candidateList=candi.get();
        candidateList.setEmailVerificationToken(token);
        candidateList.setTokenCreationDate(LocalDateTime.now());
        candidateListRepository.save(candidateList);
        String link = "http://localhost:8080/common/emailTokenVerification?token=" +token;
        String subject = "Here's the link to verify your application";
        String content =
//                "<p>Hello,</p>"
//                + "<p>You have requested to reset your password.</p>"
                 "<p>Click the link below to verify your application:</p>"
                + "<p><a href=\"" + link + "\">Verify</a></p>"
//                + "<br>"
//                + "<p>Ignore this email if you have not made the request.</p>"
                ;
//        EmailDetails ED=new EmailDetails();
////        ED.setSubject( subject);
////        ED.setRecipient(gmail);
//        ED.setMsgBody(content);
//        emailService.sendHtmlMail(ED);
        emailQueue EQ=new emailQueue();
        EQ.setSubject( subject);
        EQ.setToEmails(gmail);
        EQ.setMessage(content);
        EQ.setStatus("pending");
        EQ.setFromEmail(sender);
        EQ.setNoofAttempts(0);
        emailQueueRepository.save(EQ);
    }

    public void resendEmailVerification(String gmail){
        Optional<candidateList> candi=candidateListRepository.findByGmail(gmail);
        candidateList candidateList=candi.get();
        String link = "http://localhost:8080/common/emailTokenVerification?token=" +candidateList.getEmailVerificationToken();
        String subject = "Here's the link to verify your application";
        String content = "<p>Hello,</p>"
                + "<p>Click the link below to verify your application:</p>"
                + "<p><a href=\"" + link + "\">Verify</a></p>"
                + "<br>"
                + "<p>Ignore this email if you have not made the request.</p>";
        EmailDetails ED=new EmailDetails();
        ED.setSubject( subject);
        ED.setRecipient(gmail);
        ED.setMsgBody(content);
        emailService.sendHtmlMail(ED);

    }

    public RedirectView emailTokenVerification(String token){
        candidateList candiOptional = (candidateListRepository.findByEmailVerificationToken(token));

        if (candidateListRepository.findByEmailVerificationToken(token)==null) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("http://localhost:8080/tokenNotFound");

            return redirectView;
        }

        LocalDateTime tokenCreationDate = candiOptional.getTokenCreationDate();

        if (resetPasswordTokenUtil.isTokenExpired(tokenCreationDate)) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("http://localhost:8080/resendToken.html");

            return redirectView;

        }
        RedirectView redirectView = new RedirectView();
        candidateList candidateList=candiOptional;
        candidateList.setEmailVerified(true);
        candidateList.setEmailVerificationToken(null);
        candidateList.setTokenCreationDate(null);
        candidateListRepository.save(candidateList);
        redirectView.setUrl("http://localhost:3000/emailVerify");

        return redirectView;
    }

    public boolean check(int id) {
        if (voterListRepository.findById(id).isPresent()) {
            return true;
        }
        return false;
    }

//    public String whichEvent() {
//        return (eventNo.getEventNumber());
//    }


    public String uploadUserImage(int id, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Optional<user> userOptional= userRepository.findById(id);
            user us=userOptional.get();
        imageStorageService.saveImage("user/"+String.valueOf(id),multipartFile);
            us.setProfileImage(imageStorageService.getUserImageUrl(String.valueOf(id),fileName));
            userRepository.save(us);
            return "image uploaded successfully";
        }

        public String deleteUserImage(int id){
            Optional<user> userOptional= userRepository.findById(id);
            user us=userOptional.get();
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .scheme("http").host("localhost:8080").path("/common/getUserImage?id="+0+"&filename="+"default.jpg").build();
//            us.setProfileImage(String.valueOf(uriComponents));
            us.setProfileImage(null);
            userRepository.save(us);
            return "image deleted successfully";
        }

//    public Page<candidateList> getcandidate(int pageNo, int size){
//        Pageable Page = PageRequest.of(pageNo, size);
//        return candidateListRepository.findAllByIsAdminVerified(true,Page);
//    }

    public Page<candidate> getCandidate(int pageNo, int size,String key){
        Pageable page = PageRequest.of(pageNo, size);
        if(key==null) {
            return candidateRepository.findAll(page);
        }
        return candidateRepository.search(key,page);
    }


    public Page<user> getUser(int page,int size,String[] sort,String key) {

        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageRequest = PageRequest.of(page, size, sorting);
        if(key==null) {

//        Pageable sortedByPriceDescNameAsc =
//                PageRequest.of(0, 5, Sort.by("price").descending().and(Sort.by("name")));
//        Pageable pagingSort = PageRequest.of(page, size, Sort.by("name"));

            return userRepository.findAll(pageRequest);
        }
        return userRepository.search(key,pageRequest);
    }

    public Optional<com.example.voter_engine.Entity.eventEntity> EventDetail(){
        return eventEntityRepository.findById(1);
    }

    public user getUserByEmail(String gmail){
        return userRepository.findByGmail(gmail);
    }

    public String editUser(int id,userPojo userPojo){

        Optional<user> userOptional = userRepository.findById(id);
        user us=userOptional.get();
        us.setUserName(userPojo.getUserName());
        us.setGmail(userPojo.getGmail());
        us.setAbout(userPojo.getAbout());
        us.setAddress(userPojo.getAddress());
        us.setGender(userPojo.getGender());
        us.setPhone(userPojo.getPhone());
//        us.setPassword(userPojo.getPassword());
//        us.setPassword(Utility.generateSecurePassword());
//        UriComponents uriComponents = UriComponentsBuilder.newInstance()
//                .scheme("http").host("localhost:8080").path("/common/getUserImage?id="+0+"&filename="+"default.jpg").build();
//        us.setProfileImage(String.valueOf(uriComponents));
        userRepository.save(us);
        return "saved";
    }

}
