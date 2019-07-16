import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

@CucumberOptions(
    features = "src/test/resources/features",
    plugin = {
        "pretty", "html:target/cucumber-reports"
    }
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {

    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<WebDriver>() {
        @Override
        protected WebDriver initialValue() {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setBinary("/usr/bin/google-chrome");
            chromeOptions.addArguments("--headless");
            System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver");
            ChromeDriver driver = new ChromeDriver(chromeOptions);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            return driver;
        }
    };

    public static WebDriver getWebDriver() {
        return threadLocalDriver.get();
    }

}
