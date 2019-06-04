package org.openapitools.api.factories;

import org.openapitools.api.impl.TracksApiServiceImpl;
import org.openapitools.api.TracksApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class TracksApiServiceFactory {
    private final static TracksApiService service = new TracksApiServiceImpl();

    public static TracksApiService getTracksApi() {
        return service;
    }
}
