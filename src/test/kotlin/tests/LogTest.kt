package tests

import org.junit.jupiter.api.Test

import org.tinylog.Logger

class LogTest {
    @Test
    fun logSomething() {
        // Logger.trace("trace" as Any)
        // Logger.debug("debug" as Any)
        // Logger.info("info" as Any)
        // Logger.warn("warning" as Any)
        // Logger.error("error" as Any)
        Logger.tag("POSITION").info("print the position" as Any)
    }
}
