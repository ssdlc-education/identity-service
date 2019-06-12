package com.yahoo.identity.services.storage.sql;

public class AccountModel {
    private int uid;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String description;
    private long createTs;
    private long updateTs;

    public int getUid() { return uid; }

    public void setUid(int uid) { this.uid = uid; }

    public String getUsername() { return this.username; }

    public void setUsername(String username) { this.username = username; }

    public String getFirstname() { return this.firstname; }

    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return this.lastname; }

    public void setLastname(String lastname) { this.lastname = lastname; }

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
