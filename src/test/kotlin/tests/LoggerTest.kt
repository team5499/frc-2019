package tests

import org.junit.jupiter.api.Test

import org.tinylog.Logger
import org.tinylog.configuration.Configuration

public class LoggerTest {

    @Test
    public fun testLogger() {
        Configuration.set("writer1", "console")
        Configuration.set("writer1.level", "debug")
        Configuration.set("writer1.buffered", "true")

        Logger.debug("yeet", 1)
        Logger.debug("you dumb as hell", "")
    }
}
