// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package org.openapitools.api.factories;

import org.openapitools.api.VerificationsApiService;
import org.openapitools.api.impl.VerificationsApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class VerificationsApiServiceFactory {
    public static VerificationsApiService getVerificationsApi() {
        return SingletonIdentityFactory.verificationApiService;
    }
}
