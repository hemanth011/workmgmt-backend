package com.workmgmt.workmgmt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String userName) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to WorkMgmt");
        message.setText(
                "Hi " + userName + ",\n\n" +
                        "Welcome to WorkMgmt!\n\n" +
                        "Thank you for registering with us.\n" +
                        "We’re excited to have you on board.\n\n" +
                        "Regards,\n" +
                        "WorkMgmt Team"
        );

        mailSender.send(message);
    }
    public void sendProjectAssignmentEmail(String toEmail,
                                           String userName,
                                           String projectName) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("You have been added to a project");
        message.setText(
                "Hi " + userName + ",\n\n" +
                        "You have been added to the project: " + projectName + ".\n\n" +
                        "We’re excited to have you collaborate with the team.\n\n" +
                        "Regards,\n" +
                        "WorkMgmt Team"
        );

        mailSender.send(message);
    }

}
