package com.vox.services.storage.sql;

import com.vox.Track;
import com.vox.TrackCreate;
import com.vox.VoxError;
import com.vox.VoxException;
import com.vox.services.id.IDService;
import com.vox.services.storage.Storage;
import com.vox.services.system.SystemService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.annotation.Nonnull;
import java.io.InputStream;

public class SqlStorage implements Storage {

    private final SystemService systemService;
    private final SqlSessionFactory sqlSessionFactory;
    private final IDService idService;

    public SqlStorage(@Nonnull IDService idService, @Nonnull SystemService systemService) {
        this.idService = idService;
        this.systemService = systemService;
        this.sqlSessionFactory = createSessionFactory();
    }

    @Nonnull
    @Override
    public TrackCreate newTrackCreate() {
        String trackId = idService.createTrackId();
        return new SqlTrackCreate(trackId, sqlSessionFactory);
    }

    @Nonnull
    @Override
    public Track getTrack(@Nonnull String id) {
        return null;
    }

    @Nonnull
    private SqlSessionFactory createSessionFactory() {

        try {
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            InputStream inputStream = systemService.getResourceAsStream("mybatis-config.xml");
            return builder.build(inputStream);
        } catch (Exception ex) {
            throw new VoxException(VoxError.INTERNAL_SERVER_ERROR, "Fail to create session factory", ex);
        }
    }
}
