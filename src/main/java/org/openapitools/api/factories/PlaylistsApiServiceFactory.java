package org.openapitools.api.factories;

import org.openapitools.api.impl.PlaylistsApiServiceImpl;
import org.openapitools.api.PlaylistsApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class PlaylistsApiServiceFactory {
    private final static PlaylistsApiService service = new PlaylistsApiServiceImpl();

    public static PlaylistsApiService getPlaylistsApi() {
        return service;
    }
}
