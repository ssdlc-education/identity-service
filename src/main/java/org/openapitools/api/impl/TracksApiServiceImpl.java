package org.openapitools.api.impl;

import com.vox.DefaultVoxFactory;
import com.vox.TrackCreate;
import com.vox.Vox;
import com.vox.VoxFactory;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.api.TracksApiService;
import org.openapitools.model.Track;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class TracksApiServiceImpl extends TracksApiService {

    private final Vox vox;

    public TracksApiServiceImpl() {
        VoxFactory voxFactory = new DefaultVoxFactory();
        this.vox = voxFactory.create();
    }

    @Override
    public Response createTrack(Track track, SecurityContext securityContext) throws NotFoundException {
        TrackCreate trackCreate = vox.newTrackCreate();
        trackCreate.setTitle(track.getTitle());
        String id = trackCreate.create();
        Track respTrack = new Track();
        respTrack.setId(id);
        return Response.ok().entity(respTrack).build();
    }
    @Override
    public Response deleteTrack( @Pattern(regexp="^\\d+$")String id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getTrack( @Pattern(regexp="^\\d+$")String id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getTracks( List<String> playlists,  String cursor,  @Min(0) @Max(100) Integer limit, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updateTrack( @Pattern(regexp="^\\d+$")String id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
