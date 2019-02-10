package tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Tag

import org.team5499.frc2019.Constants

import org.team5499.dashboard.Dashboard

@Tag("dashboard")
public class DashboardTest {

    @Test
    public fun playgroundTest() {
        Dashboard.start(this, "DashboardConfig.json")
        Constants.initConstants()
        while (true) {
            Thread.sleep(1000)
        }
    }
}
