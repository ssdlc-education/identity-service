package com.yahoo.identity.services.storage.sql;

public class AccountModel {
    private String uid;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String description;
    private long createTs;
    private long updateTs;

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getUsername() { return this.username; }

    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return this.firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return this.lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }

    public long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(long createTs) {
        this.createTs = createTs;
    }

    public long getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(long updateTs) {
        this.updateTs = updateTs;
    }
}
