package com.vox.services.storage.sql;

import org.apache.ibatis.annotations.Param;

public interface TrackMapper {

    int insertTrack(@Param("track") TrackModel track);
}
