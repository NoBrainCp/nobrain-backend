package com.nobrain.bookmarking.infra.certification.mail.controller;

import com.nobrain.bookmarking.infra.certification.dto.CertificationRequest;
import com.nobrain.bookmarking.infra.certification.mail.dto.MailRequest;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import com.nobrain.bookmarking.infra.certification.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import static com.nobrain.bookmarking.global.error.ErrorCode.INVALID_AUTH_CODE;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}/mails")
public class MailController {

    private final MailService mailService;
    private final ResponseService responseService;

    @GetMapping("/{mail}/auth-code")
    public CommonResult sendAuthenticationMail(@PathVariable String mail) throws MessagingException {
        mailService.sendEmailForAuthentication(mail);
        return responseService.getSuccessResult();
    }

    @PostMapping("/{mail}/auth-code/password")
    public CommonResult sendEmailAndCode(@PathVariable String mail, @RequestBody CertificationRequest.Code requestDto) {
        if (mailService.verifyEmailCode(mail, requestDto.getCode())) {
            return responseService.getSuccessResult();
        }

        return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }

    @PostMapping("/{mail}/auth-code/login-id")
    public CommonResult sendEmailAndLoginId(@PathVariable String mail, @RequestBody CertificationRequest.Code requestDto) {
        if (mailService.verifyEmailCode(mail, requestDto.getCode())) {
            mailService.sendUserLoginIdAsEMail(mail);
            return responseService.getSuccessResult();
        }

        return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }

    @PostMapping("/users")
    public CommonResult sendMailToUsers(@RequestBody MailRequest.MultipleMail dto) {
        mailService.sendEmailToUsers(dto.getMails(), dto.getSubject(), dto.getText());
        return responseService.getSuccessResult();
    }
}
