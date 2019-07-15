package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.storage.AccountModel;
import com.verizonmedia.identity.services.storage.Storage;
import com.verizonmedia.identity.services.system.SystemService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Verifications;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for {@link AccountCreateImpl}.
 */
public class AccountCreateImplTest {

    @Injectable
    private Storage storage;

    @Injectable
    private SystemService systemService;

    @Injectable
    private PasswordService passwordService;

    private AccountCreateImpl accountCreate;

    @BeforeMethod
    public void setUp() {
        accountCreate = new AccountCreateImpl(
            storage,
            passwordService,
            systemService
        );
    }

    @Test
    public void testCreate() {

        new Expectations() {{
            systemService.currentTimeMillis();
            result = 1563200810123L;
            times = 1;

            passwordService.createPasswordHash("test_pwd");
            result = "test_pwd_hash";
            times = 1;
        }};

        String result = accountCreate
            .setUsername("my_user_name")
            .setFirstName("my_first_name")
            .setLastName("my_last_name")
            .setEmail("test@example.com")
            .setPassword("test_pwd")
            .setDescription("test_desp")
            .create();

        new Verifications(){{
            AccountModel accountModel;
            storage.createAccount(accountModel = withCapture());
            times = 1;
            Assert.assertEquals(accountModel.getUsername(), "my_user_name");
            Assert.assertEquals(accountModel.getFirstName(), "my_first_name");
            Assert.assertEquals(accountModel.getLastName(), "my_last_name");
            Assert.assertEquals(accountModel.getLastName(), "my_last_name");
            Assert.assertEquals(accountModel.getEmail(), "test@example.com");
            Assert.assertEquals(accountModel.getPasswordHash(), "test_pwd_hash");
            Assert.assertEquals(accountModel.getDescription(), "test_desp");
            Assert.assertEquals(accountModel.getCreateTs(), 1563200810123L);
            Assert.assertEquals(accountModel.getUpdateTs(), 1563200810123L);
            Assert.assertFalse(accountModel.isEmailVerified());
        }};
        Assert.assertEquals(result, "my_user_name");
    }

    // TODO Add failure test cases
}
