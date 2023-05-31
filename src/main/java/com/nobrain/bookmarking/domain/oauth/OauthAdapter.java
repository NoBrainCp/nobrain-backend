package com.nobrain.bookmarking.domain.oauth;

import java.util.HashMap;
import java.util.Map;

public class OauthAdapter {

    private OauthAdapter() {}

    public static Map<String, OauthProvider> getOauthProviders(OauthProperties properties) {
        HashMap<String, OauthProvider> oauthProvider = new HashMap<>();

        properties.getAccount().forEach((key, account) -> oauthProvider.put(key,
                new OauthProvider(account, properties.getProvider().get(key))));
        return oauthProvider;
    }
}
