package com.nobrain.bookmarking.domain.certification.phone;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.domain}")
public class PhoneController {

    private final PhoneService phoneService;

    @PostMapping("/send-one")
    public SingleMessageSentResponse sendOne() {
        return phoneService.sendSingleMessage();
    }
}
