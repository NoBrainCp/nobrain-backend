package com.nobrain.bookmarking.domain.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}")
public class MailController {

    private final MailService mailService;

    @GetMapping("/mail/{mail}")
    public void sendAuthenticationMail(@PathVariable String mail) throws MessagingException {
        mailService.sendEmail(mail);
    }
}
