package com.yahoo.identity.services.storage.sql;

import static com.kosprov.jargon2.api.Jargon2.jargon2Hasher;

import com.yahoo.identity.services.storage.AccountImpl;
import com.yahoo.identity.services.storage.AccountModel;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.apache.ibatis.session.SqlSessionFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Base64;

public class AccountImplTest {

    @Tested
    AccountImpl account;
    @Injectable
    SqlSessionFactory sqlSessionFactory;
    @Injectable
    AccountModel accountModel;


    String password;
    String salt;
    String hash;
    byte[] saltBytes;

    @DataProvider(name = "Fails")
    public static Object[][] fails() {
        return new Object[][]{{1}, {6}};
    }

    @BeforeMethod
    public void setup() {
        password = "00000";
        saltBytes = new byte[64];
        salt = Base64.getEncoder().encodeToString(saltBytes);
        hash = jargon2Hasher().salt(saltBytes).password(password.getBytes()).encodedHash();
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
            salt.getBytes();
            result = saltBytes;
        }};

        Assert.assertTrue(account.verify(password));

        new Expectations() {{
            accountModel.getConsecutiveFails();
            result = 1;
            accountModel.getPasswordHash();
            result = hash;
            salt.getBytes();
            result = saltBytes;
        }};
        Assert.assertTrue(account.verify(password));
    }

    @Test(dataProvider = "Fails")
    public void testVerifyUnblockedAndWrong(int fails) {
        new Expectations() {{
            accountModel.getBlockUntilTs();
            result = Instant.now().toEpochMilli();
            accountModel.getConsecutiveFails();
            result = fails;
            accountModel.getPasswordHash();
            result = hash;
            salt.getBytes();
            result = saltBytes;
        }};
        Assert.assertFalse(account.verify("12345"));
    }

    @Test
    public void testVerifyBlocked() {
        new Expectations() {{
            accountModel.getConsecutiveFails();
            result = 6;
            accountModel.getBlockUntilTs();
            result = Instant.now().plusSeconds(10000).toEpochMilli();
        }};
        Assert.assertFalse(account.verify(password));
    }
}
