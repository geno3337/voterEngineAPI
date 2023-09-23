package com.example.voter_engine.service;

import com.example.voter_engine.Entity.candidate;
import com.example.voter_engine.Entity.candidateList;
import com.example.voter_engine.Entity.user;
import com.example.voter_engine.Request.authenticationRequest;
import com.example.voter_engine.Request.resetPasswordRequest;
import com.example.voter_engine.Response.jwtResponse;
import com.example.voter_engine.expection.recordMismatchException;
import com.example.voter_engine.expection.resourceNotFound;
import com.example.voter_engine.repository.CandidateRepository;
import com.example.voter_engine.repository.ImageStorageService;
import com.example.voter_engine.security.JwtUtil;
import com.example.voter_engine.utility.resetPasswordTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class commonService {

    @Autowired
    private com.example.voter_engine.repository.userRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private com.example.voter_engine.Request.authenticationRequest authenticationRequest;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired(required = false)
    private UserDetails userDetails;
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private resetPasswordTokenUtil resetPasswordTokenUtil;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private com.example.voter_engine.repository.candidateListRepository candidateListRepository;


    public ResponseEntity<jwtResponse> createToken(authenticationRequest authenticationRequest) {
       user user1= userRepository.findByGmail(authenticationRequest.getEmail());
        if(user1==null){
            throw new resourceNotFound("Invalid email");
        }
        if(!authenticationRequest.getPassword().equals(user1.getPassword())){
            throw new recordMismatchException("Invalid password");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                authenticationRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String jwt = jwtUtil.generateToken(userDetails);
        List<String> role = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        return ResponseEntity.ok(new jwtResponse(jwt, userDetails.getUsername(), role));
    }


    public String forgotPassword(String gmail) {

        Optional<user> userOptional = Optional
                .ofNullable(userRepository.findByGmail(gmail));

        if (userRepository.findByGmail(gmail)==null) {
            throw  new resourceNotFound( "Invalid email .");
        }

        user user1 = userOptional.get();
        user1.setPasswordResetToken(resetPasswordTokenUtil.generateToken());
        user1.setPswtokenCreationDate(LocalDateTime.now());

        user1 = userRepository.save(user1);

        String resetPasswordLink = "http://localhost:8080/common/passwordResetTokenVerification?token=" + user1.getPasswordResetToken();
        resetPasswordTokenUtil.SendforgetPasswordEmail(gmail, resetPasswordLink);

        return "Link is sent to your email click the link to change password";
    }

    public  String resendPasswordToken(String gmail){

        Optional<user> userOptional = Optional
                .ofNullable(userRepository.findByGmail(gmail));

        if (userRepository.findByGmail(gmail)==null) {
            throw new resourceNotFound( "Invalid email.");
        }

        user user1 = userOptional.get();
        String resetPasswordLink = "http://localhost:8080/common/passwordResetTokenVerification?token=" + user1.getPasswordResetToken();
        resetPasswordTokenUtil.SendforgetPasswordEmail(gmail, resetPasswordLink);

        return user1.getPasswordResetToken();

    }

    public RedirectView passwordResetTokenVerification(String token){
        Optional<user> userOptional = Optional
                .ofNullable(userRepository.findByPasswordResetToken(token));

        if (!userOptional.isPresent()) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("http://userNotFound.html");

            return redirectView;
        }

        LocalDateTime tokenCreationDate = userOptional.get().getPswtokenCreationDate();

        if (resetPasswordTokenUtil.isTokenExpired(tokenCreationDate)) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("http://tokenExperied.html");

            return redirectView;

        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:3000/changePassword?token="+token);

        return redirectView;

    }

    public String resetPassword(resetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
        String token = request.getParameter("token");

        if(token==null){
            throw new RuntimeException( "token should not be null");
        }

        if (!userRepository.existsByPasswordResetToken(token)){
            throw new resourceNotFound( "token is invalid");
        }

        Optional<user> userOptional = Optional
                .ofNullable(userRepository.findByPasswordResetToken(token));

        if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConformPassword())){
            throw  new recordMismatchException( "password and conformPassword are not same");
        }

        user user = userOptional.get();

        user.setPassword(resetPasswordRequest.getPassword());
        user.setPasswordResetToken(null);
        user.setPswtokenCreationDate(null);

        userRepository.save(user);

        return "Your password successfully updated.";
    }

    public Page<candidate> getAll(Pageable page) {
        return candidateRepository.findAll(page);
    }

    public Page<candidateList> getcandidate(int pageNo,int size){
        Pageable Page = PageRequest.of(pageNo, size);
        return candidateListRepository.findAllByIsAdminVerified(true,Page);
    }

//    public List<candidate> searchCandidate(String keyword){
//        return candidateRepository.search(keyword,page);
//    }

    public UrlResource getImage(String id, String filename){
        return imageStorageService.getImage(id,filename);
    }

}
