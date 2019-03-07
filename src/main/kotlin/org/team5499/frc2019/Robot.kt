package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot

import org.team5499.dashboard.Dashboard

@Suppress("MagicNumber")
class Robot : TimedRobot(0.005) {

    override fun robotInit() {
        Dashboard.start(this, "config.json")
        Constants.initProps()

        Constants.AUTO_MODE.addInlineListener() {
            key: String, value: String ->
            println("key: $key, selected option: $value")
        }
        Dashboard.addInlineListener("Constants.Lift.KP") {
            key: String, value: Double? ->
            if (value != null) {
                // set the kP value here
                println("key: $key, kP: $value")
            }
        }
        Dashboard.addInlineListener("Constants.Lift.KI") {
            key: String, value: Double? ->
            if (value != null) {
                // set the kI value here
                println("key: $key, kI: $value")
            }
        }
        Dashboard.addInlineListener("Constants.Lift.KD") {
            key: String, value: Double? ->
            if (value != null) {
                // set the kD value here
                println("key: $key, kD: $value")
            }
        }

        Dashboard.addInlineListener("Constants.Lift.LOW_HEIGHT") {
            key: String, value: Double? ->
            // set the low height
            if (value != null) {
                println("key: $key, low height: $value")
            }
        }
    }

    override fun robotPeriodic() {
        Dashboard.update() // has to be called in a loop
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
