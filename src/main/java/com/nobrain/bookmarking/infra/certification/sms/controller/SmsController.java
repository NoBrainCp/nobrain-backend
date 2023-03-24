package com.nobrain.bookmarking.infra.certification.sms.controller;

import com.nobrain.bookmarking.infra.certification.dto.CertificationRequest;
import com.nobrain.bookmarking.infra.certification.sms.dto.SmsRequest;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import com.nobrain.bookmarking.infra.certification.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.nobrain.bookmarking.global.error.ErrorCode.INVALID_AUTH_CODE;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.domain}")
public class SmsController {

    private final SmsService phoneService;
    private final ResponseService responseService;

    @GetMapping("message/balance")
    public SingleResult<Balance> getBalance(){
        Balance balance = phoneService.getBalance();
        return responseService.getSingleResult(balance);
    }

    @GetMapping("/phone/{phoneNumber}/auth-code")
    public SingleResult<SingleMessageSentResponse> sendAuthenticationPhoneNumber(@PathVariable String phoneNumber) {
        SingleMessageSentResponse response = phoneService.sendPhoneForAuthentication(phoneNumber);
        return responseService.getSingleResult(response);
    }

    @PostMapping("/phone/{phoneNumber}/auth-code/password")
    public CommonResult sendMessageAndCode(@PathVariable String phoneNumber, @RequestBody CertificationRequest.Code dto) {
       if (phoneService.verifyPhoneNumberCode(phoneNumber, dto.getCode())){
           return responseService.getSuccessResult();
       }

       return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }

    @PostMapping("/phone/{phoneNumber}/auth-code/login-id")
    public CommonResult sendMessageAndLoginId(@PathVariable String phoneNumber, @RequestBody CertificationRequest.Code dto) {
        if (phoneService.verifyPhoneNumberCode(phoneNumber, dto.getCode())) {
            phoneService.sendUserLoginIdAsMessage(phoneNumber);
            return responseService.getSuccessResult();
        }

        return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }

    @PostMapping("/phone/users")
    public SingleResult<MultipleDetailMessageSentResponse> sendMessageToUsers(@RequestBody SmsRequest.MultipleMessage dto) {
        MultipleDetailMessageSentResponse response = phoneService.sendMessageToUsers(dto);
        return responseService.getSingleResult(response);
    }

    @PostMapping("/phone/{phoneNumber}/image")
    public SingleResult<SingleMessageSentResponse> sendMessageWithImage(@PathVariable String phoneNumber, @RequestBody SmsRequest.MmsMessage dto) throws IOException {
        SingleMessageSentResponse response = phoneService.sendMmsByResourcePath(phoneNumber, dto.getText(), dto.getResourcePath());
        return responseService.getSingleResult(response);
    }
}
