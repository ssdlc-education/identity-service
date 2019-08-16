// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package org.openapitools.api;

import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;

import java.net.HttpCookie;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

public class Cookies {

    public static final String NAME_CREDENTIAL = "V";

    private final List<HttpCookie> cookies;
    public Cookies(@Nonnull List<HttpCookie> cookies) {
        this.cookies = cookies;
    }

    @Nonnull
    public Optional<HttpCookie> getFirstByName(@Nonnull String name) {
        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public String getCredential() {
        return getFirstByName("V").orElseThrow(() -> new IdentityException(
            IdentityError.INVALID_CREDENTIAL,
            "Missing cookie or invalid cookie header"))
            .getValue();
    }
}
