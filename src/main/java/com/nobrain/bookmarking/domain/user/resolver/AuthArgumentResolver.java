package com.nobrain.bookmarking.domain.user.resolver;

import com.nobrain.bookmarking.domain.auth.service.JwtTokenProvider;
import com.nobrain.bookmarking.domain.auth.util.JwtTokenExtractor;
import com.nobrain.bookmarking.domain.user.annotation.VerifiedUser;
import com.nobrain.bookmarking.global.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(VerifiedUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        VerifiedUser verifiedUserParameterAnnotation = parameter.getParameterAnnotation(VerifiedUser.class);
        if (verifiedUserParameterAnnotation.isOptional() && !JwtTokenExtractor.hasAccessToken(request)) {
            return RoleType.GUEST;
        }

        String accessToken = JwtTokenExtractor.extract(request);
        return tokenProvider.getPayload(accessToken);
    }
}
