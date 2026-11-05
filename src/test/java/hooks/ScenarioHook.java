
package hooks;

import base.Base;
import io.cucumber.java.Before;
import io.cucumber.java.After;

public class ScenarioHook extends Base {

    @Before
    public void beforeScenario() {
        initializeBrowser();
    }

    @After
    public void afterScenario() {
        if (driver != null) {
            driver.quit();
        }
    }
}
