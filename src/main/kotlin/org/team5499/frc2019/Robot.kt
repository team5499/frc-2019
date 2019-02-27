package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.PowerDistributionPanel

@Suppress("MagicNumber")
class Robot : TimedRobot(0.005) {
    val pdp: PowerDistributionPanel

    init {
        pdp = PowerDistributionPanel()
    }

    override fun robotInit() {
        Logging.logMatchData()
    }

    override fun robotPeriodic() {
        Logging.logIterativeData(pdp)
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
