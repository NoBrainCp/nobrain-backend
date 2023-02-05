package com.nobrain.bookmarking.domain.mail;

import com.nobrain.bookmarking.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class MailService {
    
    private static final String AUTHENTICATION_MAIL_SUBJECT = "Nobrain 인증코드 메일입니다.";
    private static final long DURATION = 60 * 30L;

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String configEmail;

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

    public void sendEmail(String toEmail) throws MessagingException {
//        if (redisUtil.existData(toEmail)) {
//            redisUtil.deleteData(toEmail);
//        }

        MimeMessage emailForm = creatEmailForm(toEmail);
        mailSender.send(emailForm);
    }

    public Boolean verifyEmailCode(String email, String code) {
        String findCodeByEmail = redisUtil.getData(email);
        if (findCodeByEmail == null) {
            return false;
        }

        return findCodeByEmail.equals(code);
    }

    private MimeMessage creatEmailForm(String email) throws MessagingException {
        String authCode = generatedCode();

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(AUTHENTICATION_MAIL_SUBJECT);
        message.setFrom(configEmail);
        message.setText(setContext(authCode), "utf-8", "html");

//        redisUtil.setDataExpire(email, authCode, DURATION);
        return message;
    }

    private String setContext(String code) {
        Context context = new Context();
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        context.setVariable("code", code);

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("mail", context);
    }

    private String generatedCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }
}
