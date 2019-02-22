package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot
import java.nio.file.WatchService
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.FileSystems

import org.tinylog.Logger

@Suppress("MagicNumber")
class Robot : TimedRobot(0.005) {

    val watcher: WatchService
    val keys: HashMap<WatchKey, Path>

    init {
        watcher = FileSystems.getDefault().newWatchService()
        keys = HashMap<WatchKey, Path>()
    }

    @Suppress("MaxLineLength")
    private fun register(dir: Path) {
        val key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)
        keys.put(key, dir)
    }

    override fun robotInit() {
        register(Paths.get("/media"))
        println("registered")
    }

    override fun robotPeriodic() {
        val event = watcher.poll()
        if (event != null) {
            println("event:")
            println("$event")
            var events = event.pollEvents()
            events.forEach({
                println(it.kind())
                if (it.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.exit(1)
                }
            })
            event.reset()
            println("reset")
        }
        val startTime = System.nanoTime()
        Logger.info("test" as Any)
        val endTime = System.nanoTime()
        println("${endTime - startTime}")
    }

    override fun disabledInit() {
    }

    override fun disabledPeriodic() {
    }

    override fun autonomousInit() {
    }

    override fun autonomousPeriodic() {
    }

    override fun teleopInit() {
    }

    override fun teleopPeriodic() {
    }
}
