package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.storage.AccountImplVulnerable;
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

public class AccountImplVulnerableTest {

    @Tested
    AccountImplVulnerable accountVulnerable;
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
    public void setup() {
        password = "00000";
        hash = DigestUtils.md5Hex(password);
    }

    @Test
    public void testVerifyUnblockedAndRight() {
        new Expectations() {{
            accountModel.getBlockUntilTs();
            result = Instant.now().toEpochMilli();
            accountModel.getConsecutiveFails();
            result = 0;
            accountModel.getPasswordHash();
            result = hash;
        }};

        Assert.assertTrue(accountVulnerable.verify(password));

        new Expectations() {{
            accountModel.getConsecutiveFails();
            result = 1;
            accountModel.getPasswordHash();
            result = hash;
        }};
        Assert.assertTrue(accountVulnerable.verify(password));
    }

    @Test(expectedExceptions = NotAuthorizedException.class, dataProvider = "Fails")
    public void testVerifyUnblockedAndWrong(int fails) {
        new Expectations() {{
            accountModel.getBlockUntilTs();
            result = Instant.now().toEpochMilli();
            accountModel.getConsecutiveFails();
            result = fails;
            accountModel.getPasswordHash();
            result = hash;
        }};
        accountVulnerable.verify("12345");
    }

    @Test
    public void testVerifyBlocked() {
        new Expectations() {{
            accountModel.getConsecutiveFails();
            result = 6;
            accountModel.getBlockUntilTs();
            result = Instant.now().plusSeconds(10000).toEpochMilli();
        }};
        Assert.assertFalse(accountVulnerable.verify(password));
    }
}
