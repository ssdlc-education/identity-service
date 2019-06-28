package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.storage.AccountModel;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.apache.commons.codec.digest.DigestUtils;
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


    String password;
    String hash;

    @DataProvider(name = "Fails")
    public static Object[][] fails() {
        return new Object[][]{{1}, {6}};
    }

    @BeforeMethod
    public void setAccountUpdate() {
        password = "00000";
        hash = DigestUtils.md5Hex(password);
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