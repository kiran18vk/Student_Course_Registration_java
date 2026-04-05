package service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    // IMPORTANT: Replace with your actual email and app password
    private static final String SENDER_EMAIL = "satellitedetection@gmail.com";
    private static final String SENDER_APP_PASSWORD = "cevodlbawqdupmur";

    public static boolean sendCredentials(String recipientEmail, String rollNo, String password) {
        if (SENDER_EMAIL.equals("your_email@gmail.com")) {
            System.out
                    .println("WARN: EmailService is not configured with real credentials. Logging to console instead.");
            System.out.println("------------------------------------------------");
            System.out.println("To: " + recipientEmail);
            System.out.println("Subject: Your Student Portal Credentials");
            System.out.println("Message: ");
            System.out.println("Hello,\n\nYour account has been created. Here are your login details:");
            System.out.println("Roll Number: " + rollNo);
            System.out.println("Password: " + password);
            System.out.println("------------------------------------------------");
            return true; // Simulate success
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your Student Portal Credentials");

            String emailContent = "<h3>Welcome to the Student Portal!</h3>"
                    + "<p>Your account has been set up successfully. Here are your login details:</p>"
                    + "<ul>"
                    + "<li><b>Roll Number (Username):</b> " + rollNo + "</li>"
                    + "<li><b>Password:</b> " + password + "</li>"
                    + "</ul>"
                    + "<p>Please keep this information secure.</p>";

            message.setContent(emailContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email successfully sent to " + recipientEmail);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email to " + recipientEmail);
            return false;
        }
    }

    public static boolean sendPasswordReset(String recipientEmail, String rollNo, String password) {
        if (SENDER_EMAIL.equals("your_email@gmail.com")) {
            System.out.println("WARN: EmailService is not configured with real credentials. Logging to console instead.");
            System.out.println("------------------------------------------------");
            System.out.println("To: " + recipientEmail);
            System.out.println("Subject: Password Recovery - Student Portal");
            System.out.println("Message: ");
            System.out.println("Hello,\n\nYou recently requested to recover your password. Here are your login details:");
            System.out.println("Roll Number: " + rollNo);
            System.out.println("Password: " + password);
            System.out.println("------------------------------------------------");
            return true;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Password Recovery - Student Portal");

            String emailContent = "<h3>Password Recovery</h3>"
                    + "<p>You requested to recover your password. Here are your login details:</p>"
                    + "<ul>"
                    + "<li><b>Roll Number (Username):</b> " + rollNo + "</li>"
                    + "<li><b>Password:</b> " + password + "</li>"
                    + "</ul>"
                    + "<p>Please keep this information secure.</p>";

            message.setContent(emailContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Password recovery email successfully sent to " + recipientEmail);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send password recovery email to " + recipientEmail);
            return false;
        }
    }
}
