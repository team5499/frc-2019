package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.XboxController

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.subsystems.Lift
import org.team5499.frc2019.controllers.SandstormController
import org.team5499.frc2019.controllers.TeleopController

class Robot : TimedRobot(Constants.ROBOT_UPDATE_PERIOD) {

    // inputs
    private val mDriver:              XboxController
    private val mCodriver:            XboxController

    // subsystems
    private val mDrivetrain:          Drivetrain
    private val mLift:                Lift
    private val mSubsystemsManager:   SubsystemsManager

    // controllers
    private val mSandstormController: SandstormController
    private val mTeleopController:    TeleopController

    init {
        mDriver   = XboxController(Constants.Input.DRIVER_PORT)
        mCodriver = XboxController(Constants.Input.CODRIVER_PORT)

        mDrivetrain = Drivetrain()
        mLift = Lift()
        mSubsystemsManager = SubsystemsManager(mDrivetrain, mLift)

        mSandstormController = SandstormController(mSubsystemsManager, mDriver, mCodriver)
        mTeleopController = TeleopController(mSubsystemsManager, mDriver, mCodriver)
    }

    override fun robotInit() {
        // super.setPeriod(0.002)
        // super.period = 0.002
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