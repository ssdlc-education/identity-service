package com.yahoo.identity.services.storage.sql;

import org.apache.ibatis.annotations.Param;

public interface AccountMapper {

    int insertAccount(@Param("account") AccountModel account);
}
