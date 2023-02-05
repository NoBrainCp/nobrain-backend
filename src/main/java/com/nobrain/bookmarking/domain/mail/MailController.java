package com.nobrain.bookmarking.domain.mail;

import com.nobrain.bookmarking.domain.mail.dto.MailRequest;
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

    @GetMapping("mail/{mail}/authcode")
    public CommonResult sendAuthenticationMail(@PathVariable String mail) throws MessagingException {
        mailService.sendEmailForAuthentication(mail);
        return responseService.getSuccessResult();
    }

    @PostMapping("mail/{mail}/authcode")
    public CommonResult sendEmailAndCode(@PathVariable String mail, @RequestBody MailRequest.Authentication requestDto) {
        if (mailService.verifyEmailCode(mail, requestDto.getCode())) {
            return responseService.getSuccessResult();
        }

        return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }
}
