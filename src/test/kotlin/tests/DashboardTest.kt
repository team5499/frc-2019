package tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Tag

import org.team5499.dashboard.Dashboard
import org.team5499.frc2019.Constants

@Tag("dashboard")
public class DashboardTest {

    @Test
    public fun playgroundTest() {
        Dashboard.start(this, "DashboardConfig.json")
        Constants.pushValuesToDashboard()
        while (true) {
            Thread.sleep(1000)
            Constants.pullValuesFromDashboard()
        }
    }
}
