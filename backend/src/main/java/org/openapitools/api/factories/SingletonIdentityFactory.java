package org.openapitools.api.factories;

import com.yahoo.identity.DefaultIdentityFactory;
import com.yahoo.identity.DefaultIdentityFactoryFixed;
import com.yahoo.identity.Identity;
import com.yahoo.identity.IdentityFactory;

import javax.annotation.Nonnull;

public class SingletonIdentityFactory {

    private static final Identity identity;

    static {
        IdentityFactory factory = new DefaultIdentityFactory();
        identity = factory.create();
    }

    @Nonnull
    public static Identity get() {
        return identity;
    }
}
