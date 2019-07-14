package org.openapitools.api.factories;

import com.verizonmedia.identity.DefaultIdentityFactory;
import com.verizonmedia.identity.Identity;
import com.verizonmedia.identity.IdentityFactory;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.CookieParser;
import org.openapitools.api.SessionsApiService;
import org.openapitools.api.TokensApiService;
import org.openapitools.api.VerificationsApiService;
import org.openapitools.api.impl.AccountsApiServiceImpl;
import org.openapitools.api.impl.SessionsApiServiceImpl;
import org.openapitools.api.impl.TokensApiServiceImpl;
import org.openapitools.api.impl.VerificationsApiServiceImpl;

import javax.annotation.Nonnull;

public class SingletonIdentityFactory {

    public static final AccountsApiService accountsApiService;
    public static final SessionsApiService sessionsApiService;
    public static final TokensApiService tokenApiService;
    public static final VerificationsApiService verificationApiService;

    static {
        // To change to the version with vulnerabilities fixed, change to
        // IdentityFactory factory = new DefaultIdentityFactoryFixed();
        IdentityFactory factory = new DefaultIdentityFactory();
        Identity identity = factory.create();
        CookieParser cookieParser = new CookieParser();
        // To change to the version with vulnerabilities fixed, change to
        // accountsApiService = new AccountsApiServiceImplFixed(identity, cookieParser);
        accountsApiService = new AccountsApiServiceImpl(identity, cookieParser);
        sessionsApiService = new SessionsApiServiceImpl(identity);
        tokenApiService = new TokensApiServiceImpl(identity, cookieParser);
        verificationApiService = new VerificationsApiServiceImpl(identity);
    }

}
