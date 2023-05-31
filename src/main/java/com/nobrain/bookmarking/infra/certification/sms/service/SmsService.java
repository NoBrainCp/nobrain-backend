package com.nobrain.bookmarking.infra.certification.sms.service;

import com.nobrain.bookmarking.infra.certification.sms.dto.SmsRequest;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class SmsService {

    private static final String AUTHENTICATION_MESSAGE_SUBJECT = "No Brain 인증코드 입니다.";
    private static final String SEND_LOGIN_ID_MESSAGE_SUBJECT = "No Brain 아이디 찾기";
    private static final long DURATION = 60 * 30L;

    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private DefaultMessageService messageService;

    @Value("${COOLSMS.API_KEY}")
    private String API_KEY;

    @Value("${COOLSMS.API_SECRET_KEY}")
    private String API_SECRET_KEY;

    @Value("${COOLSMS.PHONE_NUMBER}")
    private String MANAGER_PHONE_NUMBER;

    @PostConstruct
    protected void init() {
        messageService = NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET_KEY, "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendPhoneForAuthentication(String phoneNumber) {
        if (redisUtil.existData(phoneNumber)) {
            redisUtil.deleteData(phoneNumber);
        }

        return sendAuthenticationMessage(phoneNumber);
    }

    public Boolean verifyPhoneNumberCode(String phoneNumber, String code) {
        String findCodeByPhoneNumber = redisUtil.getData(phoneNumber);
        if (findCodeByPhoneNumber == null) {
            return false;
        }

        return findCodeByPhoneNumber.equals(code);
    }

    public SingleMessageSentResponse sendAuthenticationMessage(String phoneNumber) {
        String authCode = generatedCode();
        String text = AUTHENTICATION_MESSAGE_SUBJECT + "\n" + "CODE: [" + authCode + "]";
        Message message = createMessage(phoneNumber, text);

        redisUtil.setDataExpire(phoneNumber, authCode, DURATION);
        return messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public void sendUserLoginIdAsMessage(String phoneNumber) {
        String loginId = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new UserNotFoundException(phoneNumber)).getLoginId();
        String text = SEND_LOGIN_ID_MESSAGE_SUBJECT + "\n" + "LOGIN ID: [" + loginId + "]";
        Message message = createMessage(phoneNumber, text);

        messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public MultipleDetailMessageSentResponse sendMessageToUsers(SmsRequest.MultipleMessage dto) {
        ArrayList<Message> messageList = new ArrayList<>();

        for (String phoneNumber : dto.getPhoneNumbers()) {
            messageList.add(createMessage(phoneNumber, dto.getText()));
        }

        try {
            return messageService.send(messageList, false, true);
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException | NurigoUnknownException e) {
            throw new RuntimeException(e);
        }
    }

    public SingleMessageSentResponse sendMmsByResourcePath(String phoneNumber, String text, String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        File file = resource.getFile();
        String imageId = messageService.uploadFile(file, StorageType.MMS, null);
        Message message = createMessage(phoneNumber, text);
        message.setImageId(imageId);

        return messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public Balance getBalance() {
        return messageService.getBalance();
    }

    private Message createMessage(String phoneNumber, String text) {
        Message message = new Message();
        message.setFrom(MANAGER_PHONE_NUMBER);
        message.setTo(phoneNumber);
        message.setText(text);

        return message;
    }

    private String generatedCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }
}


