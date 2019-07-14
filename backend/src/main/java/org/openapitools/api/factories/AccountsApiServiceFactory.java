package org.openapitools.api.factories;

import org.openapitools.api.AccountsApiService;
import org.openapitools.api.CookieParser;
import org.openapitools.api.impl.AccountsApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class AccountsApiServiceFactory {
    public static AccountsApiService getAccountsApi() {
        return SingletonIdentityFactory.accountsApiService;
    }
}
