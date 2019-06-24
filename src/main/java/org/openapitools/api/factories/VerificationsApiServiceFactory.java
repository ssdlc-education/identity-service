package org.openapitools.api.factories;

import org.openapitools.api.VerificationsApiService;
import org.openapitools.api.impl.VerificationsApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class VerificationsApiServiceFactory {

    private final static VerificationsApiService
        service =
        new VerificationsApiServiceImpl(SingletonIdentityFactory.get());

    public static VerificationsApiService getVerificationsApi() {
        return service;
    }
}
