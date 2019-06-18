package com.yahoo.identity.services.storage.sql;

import org.apache.ibatis.annotations.Param;

public interface AccountMapper {
    int insertAccount(@Param("account") AccountModel account);

    AccountModel getAccount(@Param("username") String username);

    int updateAccount(@Param("account") AccountModel account);

    int verifySession(@Param("username") String username, @Param("password") String password);

    int verifyUsername(@Param("username") String username);
}
