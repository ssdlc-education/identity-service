package com.vox.services.storage.sql;

import com.vox.TrackCreate;
import com.vox.VoxException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Nonnull;
import java.time.Instant;

public class SqlTrackCreate implements TrackCreate {

    private final SqlSessionFactory sqlSessionFactory;
    private final TrackModel track = new TrackModel();

    public SqlTrackCreate(@Nonnull String id, @Nonnull SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        track.setId(id);
    }

    @Override
    @Nonnull
    public TrackCreate setCreateTime(@Nonnull Instant createTime) {
        track.setCreateTs(createTime.toEpochMilli());
        return this;
    }

    @Override
    @Nonnull
    public TrackCreate setUpdateTime(@Nonnull Instant updateTime) {
        track.setUpdateTs(updateTime.toEpochMilli());
        return this;
    }

    @Nonnull
    @Override
    public TrackCreate setTitle(@Nonnull String title) {
        track.setTitle(title);
        return this;
    }

    @Nonnull
    @Override
    public String create() throws VoxException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            TrackMapper mapper = session.getMapper(TrackMapper.class);
            mapper.insertTrack(track);
            session.commit();
        }
        return track.getId();
    }
}
