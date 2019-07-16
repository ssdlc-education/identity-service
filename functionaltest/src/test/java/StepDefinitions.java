import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class StepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(StepDefinitions.class);
    private WebDriver driver;
    private WebDriverWait driverWait;

    private static final String SERVICE_URL = "http://identity-frontend:5000";

    @Before
    public void before() {
        driver = RunCucumberTest.getWebDriver();
        driverWait = RunCucumberTest.getWebDriverWait();
    }

    private String pageNameToURL(String pageName) {
        String path;
        switch (pageName) {
            case "home":
                path = "";
                break;
            default:
                throw new IllegalArgumentException("Invalid page name " + pageName);
        }
        return SERVICE_URL + path;
    }

    @When("^I go to \"([^\"]*)\" page$")
    public void goToPage(String pageName) {
        String url = pageNameToURL(pageName);
        driver.get(url);
        logger.info("Go to page {}", url);
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
        Assert.assertTrue(driverWait.until(ExpectedConditions.urlToBe(url)));
    }

}
