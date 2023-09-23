package com.example.voter_engine.scheduler;

import com.example.voter_engine.Entity.emailQueue;
import com.example.voter_engine.pojo.EmailDetails;
import com.example.voter_engine.repository.EmailQueueRepository;
import com.example.voter_engine.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@ConditionalOnProperty(prefix = "schedulers.emailqueue", name = "enabled", havingValue = "true")
public class EmailScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailQueueRepository emailQueueRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Scheduled(fixedDelay = 1000, initialDelay = 15000)
    public void processEmailQueue() {
        emailQueue email = emailService.getNextPendingEmail();
        if (email != null) {
            email = updateAsInProcess(email);
            initiatedEmailSendingProcess(email);

        }
    }

    private void initiatedEmailSendingProcess(emailQueue email) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
                sendEmail(email);
        });
        executorService.shutdown();
    }

//    public void sendEmail(emailQueue emailData) throws MailException {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(emailData.getToEmails());
//            message.setSubject(emailData.getSubject());
//            message.setText(emailData.getMessage());
//            javaMailSender.send(message);
//        } catch (MailException e) {
//            // Handle the exception here, e.g., log it or rethrow it as a custom exception
//            throw new RuntimeException("Failed to send email", e);
//        }
//    }

    public void sendEmail(emailQueue emailData) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setTo(emailData.getToEmails());
            mimeMessageHelper.setText(emailData.getMessage());
            mimeMessageHelper.setSubject(emailData.getSubject());
            mimeMessageHelper.setFrom(emailData.getFromEmail(), "Voter Engine");
            javaMailSender.send(message);
            updateAsSent(emailData);

    }catch (MessagingException e) {
//            throw new RuntimeException(e);
            updateAsFailed(emailData, e.getMessage());
        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
            updateAsFailed(emailData, e.getMessage());
        }

    }

    private emailQueue updateAsInProcess(emailQueue email) {
        email.setStatus("IN_PROCESS");
        email.setNoofAttempts(0);
        return emailQueueRepository.save(email) ;
    }

    private void updateAsSent(emailQueue email) {
        email.setStatus("COMPLETE");
        email.setNoofAttempts(email.getNoofAttempts() + 1);
        emailQueueRepository.save(email);
    }

    private void updateAsFailed(emailQueue email, String description) {
        email.setStatus("FAILED");
        email.setNoofAttempts(email.getNoofAttempts() + 1);
        email.setDescription(description);
        emailQueueRepository.save(email);
    }

}
