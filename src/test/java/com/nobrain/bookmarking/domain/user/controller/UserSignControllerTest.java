package com.nobrain.bookmarking.domain.user.controller;

import com.nobrain.bookmarking.docs.RestDocsTestSupport;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.nobrain.bookmarking.Constants.*;
import static com.nobrain.bookmarking.config.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserSignControllerTest extends RestDocsTestSupport {

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
                        .content(createJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING)
                                            .description("이름")
                                            .attributes(field("constraints", "길이 2이상 8이하")),
                                    fieldWithPath("email").type(JsonFieldType.STRING)
                                            .description("이메일")
                                            .attributes(field("constraints", "유효한 이메일 형식")),
                                    fieldWithPath("password")
                                            .description("비밀번호")
                                            .attributes(field("constraints", "영문 소문자와 특수기호 1개 포함 최소 8자")),
                                    fieldWithPath("passwordCheck")
                                            .description("비밀번호 확인")
                                            .attributes(field("constraints", "비밀번호와 일치"))
                                            .optional()
                            )
                        )
                );
//        result.andExpect(status().isCreated())
//                .andDo(document("sign-up",
//                        getDocumentRequest(),
//                        getDocumentResponse(),
//                        requestFields(
//                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
//                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
//                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
//                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING).description("비밀번호 확인")
//                        ),
//                        responseFields(
//                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
//                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("유저 아이디")
//                        )
//                ));
    }
}