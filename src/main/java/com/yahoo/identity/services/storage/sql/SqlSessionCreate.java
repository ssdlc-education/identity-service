package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.session.SessionCreate;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

public class SqlSessionCreate implements SessionCreate {
    private final SqlSessionFactory sqlSessionFactory;
    private final SessionModel session = new SessionModel();

    public SqlSessionCreate(@Nonnull SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    @Nonnull
    public SessionCreate setUsername(@Nonnull String username) {
        session.setUsername(username);
        return this;
    }

    @Override
    @Nonnull
    public SessionCreate setPassword(@Nonnull String password){
        session.setPassword(password);
        return this;
    }

    @Override
    @Nonnull
    public SessionCreate setCredential(@Nonnull String credStr){
        session.setCredential(credStr);
        return this;
    }

    @Override
    @Nonnull
    public SessionCreate initCredential(){
        session.initCredential();
        return this;
    }

    @Override
    @Nonnull
    public String create(){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            if (mapper.verifySession(this.session.getUsername(), this.session.getPassword()) == 1){
                session.commit();
                return this.session.getCredential().toString();
            }
            throw new NoSuchElementException();
        }
        catch (Exception e){
            return e.toString();
        }

    }
}
