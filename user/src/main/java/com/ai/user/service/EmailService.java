package com.ai.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ai.user.model.Invitation;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendInvitationEmail(Invitation invitation) {

        System.out.println();
        System.out.println("========================================");
        System.out.println("STARTING EMAIL SEND");
        System.out.println("To: " + invitation.getEmail());
        System.out.println("Role: " + invitation.getRole());
        System.out.println("Frontend URL: " + frontendUrl);
        System.out.println("========================================");

        try {

            String invitationLink =
                    frontendUrl
                            + "/register?token="
                            + invitation.getToken();

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(invitation.getEmail());

            message.setSubject(
                    "You're invited to join the organization"
            );

            message.setText(
                    "Hello,\n\n"
                    + "You have been invited to join an organization "
                    + "as a " + invitation.getRole() + ".\n\n"

                    + "Click the link below to accept your invitation "
                    + "and create your account:\n\n"

                    + invitationLink + "\n\n"

                    + "This invitation is valid until:\n"
                    + invitation.getExpiresAt() + "\n\n"

                    + "If you did not expect this invitation, "
                    + "you can safely ignore this email.\n\n"

                    + "Regards,\n"
                    + "AI Startup Knowledge System"
            );

            System.out.println("Sending email through SMTP...");

            mailSender.send(message);

            System.out.println("========================================");
            System.out.println("EMAIL SENT SUCCESSFULLY");
            System.out.println("To: " + invitation.getEmail());
            System.out.println("========================================");

        } catch (Exception e) {

            System.out.println();
            System.out.println("========================================");
            System.out.println("EMAIL SEND FAILED");
            System.out.println("Exception Type: "
                    + e.getClass().getName());
            System.out.println("Exception Message: "
                    + e.getMessage());
            System.out.println("========================================");

            e.printStackTrace();

            System.out.println("========================================");

            throw e;
        }
    }
}