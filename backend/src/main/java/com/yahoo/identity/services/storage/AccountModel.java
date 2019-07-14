package com.yahoo.identity.services.storage;

import javax.annotation.Nonnull;

public class AccountModel {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
    private String passwordHash;
    private String description;
    private long createTs;
    private long updateTs;
    private long blockUntilTs;
    private int consecutiveFails;

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

    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
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

    public void setPasswordHash(@Nonnull String passwordHash) {
        this.passwordHash = passwordHash;
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

    public void setUpdateTs(long updateTs) {
        this.updateTs = updateTs;
    }

    public long getBlockUntilTs() {
        return this.blockUntilTs;
    }

    public void setBlockUntilTs(long blockUntilTs) {
        this.blockUntilTs = blockUntilTs;
    }

    public int getConsecutiveFails() {
        return this.consecutiveFails;
    }

    public void setConsecutiveFails(int consecutiveFails) {
        this.consecutiveFails = consecutiveFails;
    }
}
