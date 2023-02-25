package com.nobrain.bookmarking.domain.certification.phone;

import com.nobrain.bookmarking.domain.certification.phone.dto.PhoneRequest;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class PhoneService {

    private static final String AUTHENTICATION_MAIL_SUBJECT = "Nobrain 인증코드 입니다.";
    private static final long DURATION = 60 * 30L;
    private final RedisUtil redisUtil;
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

    public void sendPhoneForAuthentication(String toPhoneNumber) {
        if (redisUtil.existData(toPhoneNumber)) {
            redisUtil.deleteData(toPhoneNumber);
        }

        sendSingleMessage(toPhoneNumber);
    }

    public Boolean verifyPhoneNumberCode(String phoneNumber, String code) {
        String findCodeByPhoneNumber = redisUtil.getData(phoneNumber);
        if(findCodeByPhoneNumber == null) {
            return false;
        }

        return findCodeByPhoneNumber.equals(code);
    }

    public SingleMessageSentResponse sendSingleMessage(String phoneNumber) {
        String authCode = generatedCode();
        String text = AUTHENTICATION_MAIL_SUBJECT + "\n" + "CODE: [" + authCode + "]";

        Message message = createMessage(phoneNumber, text);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        redisUtil.setDataExpire(phoneNumber, authCode, DURATION);
        return response;
    }

    public MultipleDetailMessageSentResponse sendManyMessage(PhoneRequest.MultipleMessage dto) {
        ArrayList<Message> messageList = new ArrayList<>();

        for (String phoneNumber : dto.getPhoneNumbers()) {
            messageList.add(createMessage(phoneNumber, dto.getText()));
        }

        try {
            MultipleDetailMessageSentResponse response = this.messageService.send(messageList, false, true);
            return response;
        } catch (NurigoMessageNotReceivedException e) {
            throw new RuntimeException(e);
        } catch (NurigoEmptyResponseException e) {
            throw new RuntimeException(e);
        } catch (NurigoUnknownException e) {
            throw new RuntimeException(e);
        }
    }

    public SingleMessageSentResponse sendMmsByResourcePath(String phoneNumber, String text) throws IOException {
        ClassPathResource resource = new ClassPathResource("static/sample.jpg");
        File file = resource.getFile();
        String imageId = this.messageService.uploadFile(file, StorageType.MMS, null);

        Message message = createMessage(phoneNumber, text);
        message.setImageId(imageId);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public Balance getBalance() {
        Balance balance = this.messageService.getBalance();
        return balance;
    }

    private Message createMessage(String phoneNumber, String text) {
        Message message = new Message();

        message.setFrom(FROM_PHONE_NUMBER);
        message.setTo(phoneNumber);
        message.setText(text);

        return message;
    }

    private String generatedCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

}


