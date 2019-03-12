package org.team5499.frc2019

import org.tinylog.core.LogEntry
import org.tinylog.writers.AbstractFormatPatternWriter

public class TestFileWriter(properties: Map<String, String>) : AbstractFormatPatternWriter(properties) {

    override fun write(logEntry: LogEntry) {
        println(render(logEntry))
    }

    override fun flush() {
        System.out.flush()
    }

    override fun close() {
        // System.out doesn't have to be closed
    }
}
