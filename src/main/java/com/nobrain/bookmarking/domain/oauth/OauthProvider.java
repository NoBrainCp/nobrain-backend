package com.nobrain.bookmarking.domain.oauth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthProvider {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUrl;
    private final String tokenUrl;
    private final String accountInfoUrl;

    public OauthProvider(OauthProperties.Account account, OauthProperties.Provider provider) {
        this(account.getClientId(), account.getClientSecret(), account.getRedirectUri(), provider.getTokenUri(), provider.getAccountInfoUri());
    }

    @Builder
    public OauthProvider(String clientId, String clientSecret, String redirectUrl, String tokenUrl, String accountInfoUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
        this.tokenUrl = tokenUrl;
        this.accountInfoUrl = accountInfoUrl;
    }
}
