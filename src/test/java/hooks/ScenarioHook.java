package hooks;

import java.io.ByteArrayInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import base.Base;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

public class ScenarioHook extends Base {

    private static final Logger log = LogManager.getLogger(ScenarioHook.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("Starting scenario: " + scenario.getName());
        initializeBrowser();
    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            if (driver != null && scenario.isFailed()) {
                byte[] shot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(shot, "image/png", "Failure Screenshot");
                Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(shot));
                log.error("Scenario FAILED: " + scenario.getName());
            } else {
                log.info("Scenario PASSED: " + scenario.getName());
            }
        } catch (Exception e) {
            log.error("Screenshot capture failed: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}