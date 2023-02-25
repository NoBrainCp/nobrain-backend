package com.nobrain.bookmarking.domain.certification.phone;

import com.nobrain.bookmarking.domain.certification.phone.dto.PhoneRequest;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.domain}")
public class PhoneController {

    private final PhoneService phoneService;
    private final ResponseService responseService;

    @PostMapping("/phone/{phoneNumber}/authcode")
    public SingleResult<SingleMessageSentResponse> sendOne(@PathVariable String phoneNumber) {
        SingleMessageSentResponse response = phoneService.sendSingleMessage(phoneNumber);
        return responseService.getSingleResult(response);
    }

    @PostMapping("/phone/messages")
    public SingleResult<MultipleDetailMessageSentResponse> sendMany(@RequestBody PhoneRequest.MultipleMessage dto) {
        MultipleDetailMessageSentResponse response = phoneService.sendManyMessage(dto);
        return responseService.getSingleResult(response);
    }

}
