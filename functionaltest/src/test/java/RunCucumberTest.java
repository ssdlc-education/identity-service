import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

@CucumberOptions(
    features = "src/test/resources/features",
    plugin = {
        "pretty", "html:target/cucumber-reports"
    }
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {

    private static final ThreadLocal<WebDriver> threadLocalWebDriver = ThreadLocal.withInitial(() -> {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary("/usr/bin/google-chrome");
        chromeOptions.addArguments("--headless");
        System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver");
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    });

    private static final ThreadLocal<WebDriverWait> threadLocalDriverWebDriverWait =
        ThreadLocal.withInitial(() -> new WebDriverWait(getWebDriver(), 10));

    public static WebDriver getWebDriver() {
        return threadLocalWebDriver.get();
    }

    public static WebDriverWait getWebDriverWait() {
        return threadLocalDriverWebDriverWait.get();
    }

}
