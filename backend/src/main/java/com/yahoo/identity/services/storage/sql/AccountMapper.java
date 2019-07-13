package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.storage.AccountModel;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {

    int insertAccount(@Param("account") AccountModel account);

    AccountModel getAccount(@Param("username") String username);

    AccountModel getPublicAccount(@Param("username") String username);

    int updateAccount(@Param("account") AccountModel account);
}
