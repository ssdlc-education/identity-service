package com.yahoo.identity.services.storage;

import javax.annotation.Nonnull;

public class AccountModel {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailStatus;
    private String passwordHash;
    private String passwordSalt;
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

    public boolean getEmailStatus() {
        return this.emailStatus;
    }

    public void setEmailStatus(@Nonnull boolean emailStatus) {
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

    public void setPasswordHash(@Nonnull String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return this.passwordSalt;
    }

    public void setPasswordSalt(@Nonnull String passwordSalt) {
        this.passwordSalt = passwordSalt;
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

    public Long getBlockUntilTs() {
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
}
