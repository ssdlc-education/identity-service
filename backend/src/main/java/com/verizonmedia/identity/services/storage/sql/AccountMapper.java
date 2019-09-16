// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.storage.sql;

import com.verizonmedia.identity.services.storage.AccountModel;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {

    int insertAccount(@Param("account") AccountModel account);

    AccountModel getAccount(@Param("username") String username);

    AccountModel getAccountForUpdate(@Param("username") String username);

    AccountModel getPublicAccount(@Param("username") String username);

    int updateAccount(@Param("account") AccountModel account);
}
