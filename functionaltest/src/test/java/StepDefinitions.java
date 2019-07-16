import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class StepDefinitions {
    private WebDriver driver;

    @Before
    public void before() {
        driver = RunCucumberTest.getWebDriver();
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
}
