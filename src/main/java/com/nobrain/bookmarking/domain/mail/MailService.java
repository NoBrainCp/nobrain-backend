package com.nobrain.bookmarking.domain.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MailService {
    
    private static final String AUTHENTICATION_MAIL_SUBJECT = "Nobrain 인증 메일";
    private final JavaMailSender mailSender;

    public void sendMailToUsers(List<String> emails, String subject, String text) {
        int size = emails.size();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo((String[]) emails.toArray(new String[size]));
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        mailSender.send(simpleMailMessage);
    }

    public void sendMailForAuthentication(String email, int code) {
        String text = "";
        text+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        text+= "<h3 style='color:blue;'>인증 코드입니다.</h3>";
        text+= "<div style='font-size:130%'>";
        text+= "CODE : <strong>";
        text+= code+"</strong><div><br/> ";
        text+= "</div>";

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(AUTHENTICATION_MAIL_SUBJECT);
        simpleMailMessage.setText(text);

        mailSender.send(simpleMailMessage);
    }
}
