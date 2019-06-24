package com.yahoo.identity.services.storage;

import static com.kosprov.jargon2.api.Jargon2.Hasher;
import static com.kosprov.jargon2.api.Jargon2.jargon2Hasher;
import static com.kosprov.jargon2.api.Jargon2.jargon2Verifier;

import com.kosprov.jargon2.api.Jargon2;

import java.security.SecureRandom;
import java.time.Instant;

import javax.annotation.Nonnull;

public class AccountModel {

    private String uid;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private int emailStatus;
    private String passwordHash;
    private String passwordSalt;
    private String description;
    private long createTs;
    private long updateTs;
    private long blockUntilTs;
    private int consecutiveFails;

    public String getUid() {
        return this.uid;
    }

    @Nonnull
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@Nonnull String username) {
        this.username = username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(@Nonnull String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(@Nonnull String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(@Nonnull String email) {
        this.email = email;
    }

    public int getEmailStatus() {
        return this.emailStatus;
    }

    public void setEmailStatus(@Nonnull int emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(@Nonnull String description) {
        this.description = description;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public String getPasswordSalt() {
        return this.passwordSalt;
    }

    public void setPassword(@Nonnull String password) {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(Instant.now().toString().getBytes());

        byte[] saltBytes = new byte[8];
        secureRandom.nextBytes(saltBytes);
        this.passwordSalt = saltBytes.toString();

        Hasher hasher = jargon2Hasher();
        this.passwordHash = hasher.salt(saltBytes).password(password.getBytes()).encodedHash();
    }

    public long getCreateTs() {
        return this.createTs;
    }

    public void setCreateTs(long createTs) {
        this.createTs = createTs;
    }

    public long getUpdateTs() {
        return this.updateTs;
    }

    public void setUpdateTs(@Nonnull long updateTs) {
        this.updateTs = updateTs;
    }

    public long getBlockUntilTs() {
        return this.blockUntilTs;
    }

    public void setBlockUntilTs(@Nonnull long blockUntilTs) {
        this.blockUntilTs = blockUntilTs;
    }

    public int getConsecutiveFails() {
        return this.consecutiveFails;
    }

    public void setConsecutiveFails(@Nonnull int consecutiveFails) {
        this.consecutiveFails = consecutiveFails;
    }

    @Nonnull
    public Boolean verify(@Nonnull String password) {
        Jargon2.Verifier verifier = jargon2Verifier();
        return verifier.salt(this.passwordSalt.getBytes()).hash(this.passwordHash).password(password.getBytes())
            .verifyEncoded();
    }
}
