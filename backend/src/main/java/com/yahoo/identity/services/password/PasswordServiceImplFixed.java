package com.yahoo.identity.services.password;

import com.kosprov.jargon2.api.Jargon2;
import com.yahoo.identity.services.random.RandomService;

import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

public class PasswordServiceImplFixed implements PasswordService {
    private static final int SALT_LEN = 64;
    private final RandomService randomService;

    public PasswordServiceImplFixed(@Nonnull RandomService randomService) {
        this.randomService = randomService;
    }

    @Nonnull
    @Override
    public String createPasswordHash(@Nonnull String password) {
        byte[] salt = randomService.getRandomBytes(SALT_LEN);
        Jargon2.Hasher hasher = Jargon2.jargon2Hasher();
        return hasher.salt(salt)
            .password(password.getBytes(StandardCharsets.UTF_8))
            .encodedHash();
    }

    @Override
    public boolean verifyPasswordHash(@Nonnull String password, @Nonnull String passwordHash) {
        Jargon2.Verifier verifier = Jargon2.jargon2Verifier();
        return verifier
            .hash(passwordHash)
            .password(password.getBytes(StandardCharsets.UTF_8))
            .verifyEncoded();
    }
}
