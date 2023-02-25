package com.nobrain.bookmarking.domain.certification.phone;

import com.nobrain.bookmarking.domain.certification.dto.CertificationRequest;
import com.nobrain.bookmarking.domain.certification.phone.dto.PhoneRequest;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.io.IOException;

import static com.nobrain.bookmarking.global.error.ErrorCode.INVALID_AUTH_CODE;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.domain}")
public class PhoneController {

    private final PhoneService phoneService;
    private final ResponseService responseService;

    @GetMapping("message/balance")
    public SingleResult<Balance> getBalance(){
        Balance balance = phoneService.getBalance();
        return responseService.getSingleResult(balance);
    }

    @PostMapping("/phone/{phoneNumber}/authcode")
    public CommonResult sendCodeAndLoginId(@PathVariable String phoneNumber, @RequestBody CertificationRequest.Code requestDto) {
       if(phoneService.verifyPhoneNumberCode(phoneNumber, requestDto.getCode())){
           phoneService.sendSingleMessage(phoneNumber);
           return responseService.getSuccessResult();
       }

       return responseService.getFailResult(INVALID_AUTH_CODE.getStatus(), INVALID_AUTH_CODE.getMessage());
    }

    @PostMapping("/phone/messages")
    public SingleResult<MultipleDetailMessageSentResponse> sendMany(@RequestBody PhoneRequest.MultipleMessage dto) {
        MultipleDetailMessageSentResponse response = phoneService.sendManyMessage(dto);
        return responseService.getSingleResult(response);
    }

    @PostMapping("/phone/{phoneNumber}/image")
    public SingleResult<SingleMessageSentResponse> sendMMS(@PathVariable String phoneNumber, @RequestBody PhoneRequest.SingleText dto) throws IOException {
        SingleMessageSentResponse response = phoneService.sendMmsByResourcePath(phoneNumber, dto.getText());
        return responseService.getSingleResult(response);
    }

}
