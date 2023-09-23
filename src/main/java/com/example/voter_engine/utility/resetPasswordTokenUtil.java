package com.example.voter_engine.utility;

import com.example.voter_engine.pojo.EmailDetails;
import com.example.voter_engine.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class resetPasswordTokenUtil {

    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

    @Autowired
    private EmailService emailService;


    /**
     * Generate unique token. You may add multiple parameters to create a strong
     * token.
     *
     * @return unique token
     */
    public String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    /**
     * Check whether the created token expired or not.
     *
     * @param tokenCreationDate
     * @return true or false
     */
    public boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }

    public String SendforgetPasswordEmail(String gmail,String link){
        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        EmailDetails ED=new EmailDetails();
        ED.setSubject( subject);
        ED.setRecipient(gmail);
        ED.setMsgBody(content);
        emailService.sendHtmlMail(ED);
        return"email were sended successfully";
    }



}
