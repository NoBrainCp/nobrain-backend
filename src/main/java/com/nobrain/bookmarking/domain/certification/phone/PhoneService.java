package com.nobrain.bookmarking.domain.certification.phone;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PhoneService {

    @Value("${COOLSMS.API_KEY}")
    private String API_KEY;

    @Value("${COOLSMS.API_SECRET_KEY}")
    private String API_SECRET_KEY;

    @Value("${COOLSMS.PHONE_NUMBER}")
    private String FROM_PHONE_NUMBER;

    private DefaultMessageService messageService;

    @PostConstruct
    protected void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET_KEY, "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendSingleMessage() {
        Message message = new Message();

        message.setFrom(FROM_PHONE_NUMBER);
        message.setTo("01040188963");
        message.setText("임시 메세지");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }
}
