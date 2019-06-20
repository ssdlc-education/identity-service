package com.yahoo.identity.services.session;

import static com.kosprov.jargon2.api.Jargon2.Hasher;
import static com.kosprov.jargon2.api.Jargon2.Type;
import static com.kosprov.jargon2.api.Jargon2.jargon2Hasher;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.account.AccountUpdate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;

public class AbuseDetectionImplTest {

    @Tested
    AbuseDetectionImpl abuseDetection;
    @Injectable
    Identity identity;
    @Injectable
    AccountService accountService;
    @Injectable
    Account account;
    @Injectable
    AccountUpdate accountUpdate;
    @Injectable
    Hasher hasher;

    String username;
    String password;

    @BeforeMethod
    public void setUpIdentity() {
        username = "alice";
        password = "00000";
        new Expectations() {{
            identity.getAccountService();
            result = accountService;
            accountService.getAccount(username);
            result = account;
            account.getPassword();
            result = password;
        }};
    }

    @Test
    public void testAbuseDetectionUnblockedAndRight() throws Exception {
        new Expectations(jargon2Hasher()) {{
            jargon2Hasher()
                .type(Type.ARGON2d) // Data-dependent hashing
                .memoryCost(65536)  // 64MB memory cost
                .timeCost(3)        // 3 passes through memory
                .parallelism(4)     // use 4 lanes and 4 threads
                .saltLength(16)     // 16 random bytes salt
                .hashLength(16);    // 16 bytes output hash
            result = hasher;
            account.getConsecutiveFails();
            result = 0;
            account.getBlockUntilTime();
            result = Instant.now();
            accountService.newAccountUpdate(username);
            result = accountUpdate;
            hasher.password(password.getBytes()).encodedHash();
            result = password;
        }};
        Assert.assertFalse(abuseDetection.abuseDetection(username, password));
        new Expectations() {{
            account.getConsecutiveFails();
            result = 1;
        }};
        Assert.assertFalse(abuseDetection.abuseDetection(username, password));
    }

    @Test
    public void testAbuseDetectionUnblockedAndWrong() throws Exception {
        new Expectations() {{
            account.getConsecutiveFails();
            result = 1;
            account.getBlockUntilTime();
            result = Instant.now();
            accountService.newAccountUpdate(username);
            result = accountUpdate;
        }};
        Assert.assertTrue(abuseDetection.abuseDetection(username, "12345"));
        new Expectations() {{
            account.getConsecutiveFails();
            result = 6;
        }};
        Assert.assertTrue(abuseDetection.abuseDetection(username, "12345"));
    }

    @Test
    public void testAbuseDetectionBlocked() throws Exception {
        new Expectations() {{
            account.getConsecutiveFails();
            result = 0;
            account.getBlockUntilTime();
            result = Instant.now().plusSeconds(10000);
        }};
        Assert.assertTrue(abuseDetection.abuseDetection(username, password));
        Assert.assertTrue(abuseDetection.abuseDetection(username, "12345"));
    }
}