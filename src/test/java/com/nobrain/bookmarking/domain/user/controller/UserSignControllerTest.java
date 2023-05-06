package com.nobrain.bookmarking.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.service.UserSignService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.nobrain.bookmarking.Constants.*;
import static com.nobrain.bookmarking.restdocs.ApiDocumentUtils.getDocumentRequest;
import static com.nobrain.bookmarking.restdocs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ActiveProfiles("test")
class UserSignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserSignService userSignService;

    @Test
    void signUp() throws Exception {
        // given
        given(userSignService.signUp(any(UserRequest.SignUp.class)))
                .willReturn(USER_ID);

        // when
        UserRequest.SignUp signUpRequest = new UserRequest.SignUp();
        signUpRequest.setName(USERNAME);
        signUpRequest.setEmail(EMAIL);
        signUpRequest.setPassword(PASSWORD);
        signUpRequest.setPasswordCheck(PASSWORD_CHECK);

        ResultActions result = mockMvc.perform(
                post(BASE_URL + "/sign-up")
                        .content(objectMapper.writeValueAsString(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(document("sign-up",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING).description("비밀번호 확인")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("유저 아이디")
                        )
                ));
    }
}