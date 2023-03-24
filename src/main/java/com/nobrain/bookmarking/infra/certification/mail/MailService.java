package com.nobrain.bookmarking.infra.certification.mail;

import com.nobrain.bookmarking.domain.user.exception.UserEmailNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
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
    private static final String SEND_LOGIN_ID_MESSAGE_SUBJECT = "No Brain 아이디 찾기";
    private static final String SEND_LOGIN_ID_MESSAGE_PREFIX = "ID: ";
    private static final long DURATION = 60 * 30L;

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String configEmail;

    public void sendEmailToUsers(List<String> emails, String subject, String text) {
        int size = emails.size();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo((String[]) emails.toArray(new String[size]));
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        mailSender.send(simpleMailMessage);
    }

    public void sendEmailForAuthentication(String toEmail) throws MessagingException {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }

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

    public void sendUserLoginIdAsEMail(String mail) {
        String loginId = userRepository.findByEmail(mail).orElseThrow(() -> new UserEmailNotFoundException(mail)).getLoginId();
        String text = SEND_LOGIN_ID_MESSAGE_PREFIX + loginId;
        SimpleMailMessage simpleMailMessage = createSimpleMailMessage(mail, SEND_LOGIN_ID_MESSAGE_SUBJECT, text);

        mailSender.send(simpleMailMessage);
    }

    private SimpleMailMessage createSimpleMailMessage(String mail, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        return simpleMailMessage;
    }

    private MimeMessage creatEmailForm(String email) throws MessagingException {
        String authCode = generatedCode();

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(AUTHENTICATION_MAIL_SUBJECT);
        message.setFrom(configEmail);
        message.setText(setContext(authCode), "utf-8", "html");

        redisUtil.setDataExpire(email, authCode, DURATION);
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
