package stepdefs;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;

public class step {
    public static WebDriver driver;
    @Before
    public void setup() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary("/usr/bin/google-chrome");
        chromeOptions.addArguments("--headless");
        System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Given("^I go to the page with URL \"([^\"]*)\"$")
    public void i_go_to_the_page_with_URL(String url) {
        driver.get(url);
        System.out.println(driver.getCurrentUrl());
    }

    @When("^I fill in \"([^\"]*)\" with \"([^\"]*)\"$")
    public void i_fill_in_with(String elemName, String key) {
        driver.findElement(By.name(elemName)).sendKeys(key);
    }

    @When("^I click on the \"([^\"]*)\" button$")
    public void i_click_on_the_button(String button) {
        driver.findElement(By.name(button)).click();
    }

    @Then("^I see in the field \"([^\"]*)\" there is \"([^\"]*)\"$")
    public void i_see_in_the_field_there_is(String elemName, String elemText) {
        String text = driver.findElement(By.name(elemName)).getText();
        Assert.assertEquals(text, elemText);
    }

    @Then("^I see the error message \"([^\"]*)\"$")
    public void i_see_the_error_message(String message) {
        String text = driver.findElement(By.name("errormsg")).getText();
        Assert.assertEquals(text, message);
    }

    @When("^I should be on the page with URL \"([^\"]*)\"$")
    public void i_should_be_on_the_page_with_URL(String url) {
        Assert.assertEquals(driver.getCurrentUrl(), url);
    }

    @After
    public void finish() {
        driver.quit();
    }
}
