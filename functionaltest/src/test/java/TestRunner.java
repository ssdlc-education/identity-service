import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
//import cucumber.api.testng.TestNGCucumberRunner;
//import cucumber.api.testng.CucumberFeatureWrapper;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"stepdefs"},
    tags = {"~@Ignore"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty",
        "json:target/cucumber-reports/CucumberTestReport.json",
        "rerun:target/cucumber-reports/rerun.txt"
    })
public class TestRunner extends AbstractTestNGCucumberTests {

}
