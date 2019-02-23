package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot

import org.team5499.dashboard.Dashboard

@Suppress("MagicNumber")
class Robot : TimedRobot(0.005) {

    override fun robotInit() {
        Constants.initConsts()
        Dashboard.start(this, "config.json")
        Dashboard.addVarListener("Constants.TEST_KP", {
            key: String, value: Any? ->
            println("KP: $value")
        })
        Dashboard.addVarListener("Constants.TEST_KI", {
            key: String, value: Any? ->
            println("KI: $value")
        })
        Dashboard.addVarListener("Constants.TEST_KD", {
            key: String, value: Any? ->
            println("KD: $value")
        })
    }

    override fun robotPeriodic() {
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
