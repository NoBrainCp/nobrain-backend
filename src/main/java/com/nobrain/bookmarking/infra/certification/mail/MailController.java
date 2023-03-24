package com.nobrain.bookmarking.infra.certification.mail;

import com.nobrain.bookmarking.infra.certification.dto.CertificationRequest;
import com.nobrain.bookmarking.infra.certification.mail.dto.MailRequest;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import static com.nobrain.bookmarking.global.error.ErrorCode.INVALID_AUTH_CODE;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}")
public class MailController {

    private final MailService mailService;
    private final ResponseService responseService;

    @GetMapping("/mail/{mail}/auth-code")
    public CommonResult sendAuthenticationMail(@PathVariable String mail) throws MessagingException {
        mailService.sendEmailForAuthentication(mail);
        return responseService.getSuccessResult();
    }

    @PostMapping("/mail/{mail}/auth-code/password")
    public CommonResult sendEmailAndCode(@PathVariable String mail, @RequestBody CertificationRequest.Code requestDto) {
        if (mailService.verifyEmailCode(mail, requestDto.getCode())) {
            return responseService.getSuccessResult();
        }

        return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }

    @PostMapping("/mail/{mail}/auth-code/login-id")
    public CommonResult sendEmailAndLoginId(@PathVariable String mail, @RequestBody CertificationRequest.Code requestDto) {
        if (mailService.verifyEmailCode(mail, requestDto.getCode())) {
            mailService.sendUserLoginIdAsEMail(mail);
            return responseService.getSuccessResult();
        }

        return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }

    @PostMapping("/mail/users")
    public CommonResult sendMailToUsers(@RequestBody MailRequest.MultipleMail dto) {
        mailService.sendEmailToUsers(dto.getMails(), dto.getSubject(), dto.getText());
        return responseService.getSuccessResult();
    }
}
