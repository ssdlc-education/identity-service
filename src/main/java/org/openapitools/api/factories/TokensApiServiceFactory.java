package org.openapitools.api.factories;

import org.openapitools.api.impl.TokensApiServiceImpl;
import org.openapitools.api.TokensApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class TokensApiServiceFactory {
    private final static TokensApiService service = new TokensApiServiceImpl(SingletonIdentityFactory.get());

    public static TokensApiService getTokensApi() {
        return service;
    }
}
