package com.yahoo.identity.services.key;

import javax.annotation.Nonnull;

public class KeyServiceImpl implements KeyService {
    private String secret;

    public KeyServiceImpl(){
        secret = "toySecret";
    }

    @Override
    @Nonnull
    public String getSecret() { return secret; }

    @Override
    public void setSecret(@Nonnull String secret) { this.secret = secret;}
}
