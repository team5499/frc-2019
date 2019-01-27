package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot

import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.FeedbackDevice

@Suppress("MagicNumber")
class Robot : TimedRobot(0.005) {
    private val mTalon = TalonSRX(5)

    override fun robotInit() {
        mTalon.configSelectedFeedbackSensor(FeedbackDevice.Analog)
    }

    override fun robotPeriodic() {
        println("Hall effect value: ${mTalon.getSelectedSensorPosition()}")
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
