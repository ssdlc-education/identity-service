### Functional Test

#### Prior to testing
    - install __geckodriver__ and __firefox__
    - populate mysql with accounts `Alice` and `Bob`

#### Running tests
run `mvn clean install` to run all the tests
run `mvn verify` to run all the tests and generate report
    - JSON format: located at `target/cucumber-reports/CucumberTestReport.json`
    - Pretty HTML format: located at `target/cucumber-reports/advanced-reports` directory