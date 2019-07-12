package org.openapitools.api;

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
}
