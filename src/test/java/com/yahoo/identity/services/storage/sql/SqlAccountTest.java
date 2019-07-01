package com.yahoo.identity.services.storage.sql;

import static com.kosprov.jargon2.api.Jargon2.jargon2Hasher;

import com.yahoo.identity.services.storage.AccountModel;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Base64;

import javax.ws.rs.NotAuthorizedException;

public class SqlAccountTest {

    @Tested
    SqlAccount account;
    @Injectable
    SqlSessionFactory sqlSessionFactory;
    @Injectable
    AccountMapper mapper;
    @Injectable
    AccountModel accountModel;
    @Injectable
    SqlSession sqlSession;


    String password;
    String salt;
    String hash;
    byte[] saltBytes;
    AccountModel acctModel;
    String username;


    @DataProvider(name = "Fails")
    public static Object[][] fails() {
        return new Object[][]{{1}, {6}};
    }

    @BeforeMethod
    public void setup() {
        password = "00000";
        username = "username";
        saltBytes = new byte[64];
        salt = Base64.getEncoder().encodeToString(saltBytes);
        hash = jargon2Hasher().salt(saltBytes).password(password.getBytes()).encodedHash();

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

        account.getAccount(username);
        Assert.assertEquals(account.getAccountModel().getUsername(), username);

        new Expectations() {{
           mapper.getPublicAccount(username);
           result = acctModel;
        }};
        account.getPublicAccount(username);
        Assert.assertEquals(account.getAccountModel().getUsername(), username);
    }

    @Test
    public void testGetAccountFailed() {
        new Expectations() {{
            sqlSessionFactory.openSession();
            result = new Exception();
        }};

        account.getAccount(username);
        Assert.assertNull(account.getAccountModel().getUsername());

        account.getPublicAccount(username);
        Assert.assertNull(account.getAccountModel().getUsername());
    }

    @Test
    public void testVerifyUnblockedAndRight() {
        new Expectations() {{
            account.getBlockUntilTime();
            result = Instant.now().toEpochMilli();
            account.getConsecutiveFails();
            result = 0;
            accountModel.getPasswordSalt();
            result = salt;
            accountModel.getPasswordHash();
            result = hash;
            salt.getBytes();
            result = saltBytes;
        }};

        Assert.assertTrue(account.verify(password));

        new Expectations() {{
            account.getConsecutiveFails();
            result = 1;
            accountModel.getPasswordSalt();
            result = salt;
            accountModel.getPasswordHash();
            result = hash;
            salt.getBytes();
            result = saltBytes;
        }};
        Assert.assertTrue(account.verify(password));
    }

    @Test(expectedExceptions = NotAuthorizedException.class, dataProvider = "Fails")
    public void testVerifyUnblockedAndWrong(int fails) {
        new Expectations() {{
            account.getBlockUntilTime();
            result = Instant.now().toEpochMilli();
            account.getConsecutiveFails();
            result = fails;
            accountModel.getPasswordSalt();
            result = salt;
            accountModel.getPasswordHash();
            result = hash;
            salt.getBytes();
            result = saltBytes;
        }};
        account.verify("12345");
    }

    @Test
    public void testVerifyBlocked() {
        new Expectations() {{
            account.getConsecutiveFails();
            result = 6;
            account.getBlockUntilTime();
            result = Instant.now().plusSeconds(10000).toEpochMilli();
        }};
        Assert.assertFalse(account.verify(password));
    }
}