package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.XboxController

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.controllers.SandstormController
import org.team5499.frc2019.controllers.TeleopController

class Robot : TimedRobot() {

    // inputs
    private val mDriver: XboxController
    private val mCodriver: XboxController

    // subsystems
    private val mDrivetrain: Drivetrain
    private val mSubsystemsManager: SubsystemsManager

    // controllers
    private val mSandstormController: SandstormController
    private val mTeleopController: TeleopController

    init {
        mDriver = XboxController(1)
        mCodriver = XboxController(2)

        mDrivetrain = Drivetrain()
        mSubsystemsManager = SubsystemsManager()

        mSandstormController = SandstormController(mSubsystemsManager, mDriver, mCodriver)
        mTeleopController = TeleopController(mSubsystemsManager, mDriver, mCodriver)
    }

    override fun robotInit() {
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

    override fun testInit() {

    }

    override fun testPeriodic() {

    }
}
