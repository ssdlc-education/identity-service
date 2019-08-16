// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package org.openapitools.api.impl;

import com.verizonmedia.identity.Identity;
import org.apache.commons.text.StringEscapeUtils;
import org.openapitools.api.CookieParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class AccountsApiServiceImplFixed extends AccountsApiServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(AccountsApiServiceImplFixed.class);

    public AccountsApiServiceImplFixed(@Nonnull Identity identity,
                                       @Nonnull CookieParser cookieParser) {
        super(identity, cookieParser);
    }

    protected void logAccountCreation(org.openapitools.model.Account account) {
        logger.info("Account \"{}\" created", StringEscapeUtils.escapeJava(account.getUsername()));
    }
}
