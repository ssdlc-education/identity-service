// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package org.openapitools.api;

import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;

import java.net.HttpCookie;
import java.util.List;

import javax.annotation.Nonnull;

public class CookieParser {

    @Nonnull
    public Cookies parse(String cookieStr) {
        try {
            List<HttpCookie> cookies = HttpCookie.parse(cookieStr);
            return new Cookies(cookies);
        } catch (Exception ex) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "Failed to parse the cookie");
        }
    }
}
