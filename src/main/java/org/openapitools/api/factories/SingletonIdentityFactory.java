package org.openapitools.api.factories;

import com.yahoo.identity.DefaultIdentityFactory;
import com.yahoo.identity.Identity;

import javax.annotation.Nonnull;

public class SingletonIdentityFactory {

    private static final Identity identity;

    static {
        DefaultIdentityFactory factory = new DefaultIdentityFactory();
        identity = factory.create();
    }

    @Nonnull
    public static Identity get() {
        return identity;
    }
}
