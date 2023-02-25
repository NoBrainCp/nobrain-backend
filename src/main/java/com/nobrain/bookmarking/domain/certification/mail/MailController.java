package com.nobrain.bookmarking.domain.certification.mail;

import com.nobrain.bookmarking.domain.certification.dto.CertificationRequest;
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

    @GetMapping("/mail/{mail}/authcode")
    public CommonResult sendAuthenticationMail(@PathVariable String mail) throws MessagingException {
        mailService.sendEmailForAuthentication(mail);
        return responseService.getSuccessResult();
    }

    @PostMapping("/mail/{mail}/authcode")
    public CommonResult sendEmailAndCode(@PathVariable String mail, @RequestBody CertificationRequest.Code requestDto) {
        if (mailService.verifyEmailCode(mail, requestDto.getCode())) {
            return responseService.getSuccessResult();
        }

        return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }

    @PostMapping("/mail/{mail}/authcode/email")
    public CommonResult sendEmailAndLoginId(@PathVariable String mail, @RequestBody CertificationRequest.Code requestDto) {
        if (mailService.verifyEmailCode(mail, requestDto.getCode())) {
            mailService.sendUserLoginId(mail);
            return responseService.getSuccessResult();
        }

        return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }
}
