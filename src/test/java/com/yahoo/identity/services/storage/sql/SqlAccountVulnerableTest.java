package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.storage.AccountModel;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;

import javax.ws.rs.NotAuthorizedException;

public class SqlAccountVulnerableTest {

    @Tested
    SqlAccountVulnerable accountVulnerable;
    @Injectable
    SqlSessionFactory sqlSessionFactory;
    @Injectable
    AccountModel accountModel;
    @Injectable
    SqlSession sqlSession;
    @Injectable
    AccountMapper mapper;


    String password;
    String hash;
    AccountModel acctModel;
    String username;

    @DataProvider(name = "Fails")
    public static Object[][] fails() {
        return new Object[][]{{1}, {6}};
    }

    @BeforeMethod
    public void setup() {
        password = "00000";
        hash = DigestUtils.md5Hex(password);

        acctModel = new AccountModel();
        acctModel.setUsername(username);
    }

    @Test
    public void testGetAccount() {
        new Expectations() {{
            sqlSessionFactory.openSession();
            result = sqlSession;
            mapper.getAccount(username);
            result = acctModel;
        }};

        accountVulnerable.getAccount(username);
        Assert.assertEquals(accountVulnerable.getAccountModel().getUsername(), username);

        new Expectations() {{
            mapper.getPublicAccount(username);
            result = acctModel;
        }};
        accountVulnerable.getPublicAccount(username);
        Assert.assertEquals(accountVulnerable.getAccountModel().getUsername(), username);
    }

    @Test
    public void testGetAccountFailed() {
        new Expectations() {{
            sqlSessionFactory.openSession();
            result = new Exception();
        }};

        accountVulnerable.getAccount(username);
        Assert.assertNull(accountVulnerable.getAccountModel().getUsername());

        accountVulnerable.getPublicAccount(username);
        Assert.assertNull(accountVulnerable.getAccountModel().getUsername());
    }

    @Test
    public void testVerifyUnblockedAndRight() {
        new Expectations() {{
            accountVulnerable.getBlockUntilTime();
            result = Instant.now().toEpochMilli();
            accountVulnerable.getConsecutiveFails();
            result = 0;
            accountModel.getPasswordHash();
            result = hash;
        }};

        Assert.assertTrue(accountVulnerable.verify(password));

        new Expectations() {{
            accountVulnerable.getConsecutiveFails();
            result = 1;
            accountModel.getPasswordHash();
            result = hash;
        }};
        Assert.assertTrue(accountVulnerable.verify(password));
    }

    @Test(expectedExceptions = NotAuthorizedException.class, dataProvider = "Fails")
    public void testVerifyUnblockedAndWrong(int fails) {
        new Expectations() {{
            accountVulnerable.getBlockUntilTime();
            result = Instant.now().toEpochMilli();
            accountVulnerable.getConsecutiveFails();
            result = fails;
            accountModel.getPasswordHash();
            result = hash;
        }};
        accountVulnerable.verify("12345");
    }

    @Test
    public void testVerifyBlocked() {
        new Expectations() {{
            accountVulnerable.getConsecutiveFails();
            result = 6;
            accountVulnerable.getBlockUntilTime();
            result = Instant.now().plusSeconds(10000).toEpochMilli();
        }};
        Assert.assertFalse(accountVulnerable.verify(password));
    }
}