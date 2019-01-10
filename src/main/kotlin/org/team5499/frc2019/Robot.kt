package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.subsystems.Lift
import org.team5499.frc2019.subsystems.Intake
import org.team5499.frc2019.subsystems.Vision
import org.team5499.frc2019.controllers.SandstormController
import org.team5499.frc2019.controllers.TeleopController

import org.team5499.monkeyLib.hardware.XboxControllerPlus

class Robot : TimedRobot(Constants.ROBOT_UPDATE_PERIOD) {

    // inputs
    private val mDriver: XboxControllerPlus
    private val mCodriver: XboxControllerPlus
    // subsystems
    private val mDrivetrain: Drivetrain
    private val mLift: Lift
    private val mIntake: Intake
    private val mVision: Vision
    private val mSubsystemsManager: SubsystemsManager

    // controllers
    private val mSandstormController: SandstormController
    private val mTeleopController: TeleopController

    init {
        mDriver = XboxControllerPlus(Constants.Input.DRIVER_PORT)
        mCodriver = XboxControllerPlus(Constants.Input.CODRIVER_PORT)

        mDrivetrain = Drivetrain()
        mLift = Lift()
        mIntake = Intake()
        mVision = Vision()
        mSubsystemsManager = SubsystemsManager(mDrivetrain, mLift, mIntake, mVision)

        mSandstormController = SandstormController(mSubsystemsManager, mDriver, mCodriver)
        mTeleopController = TeleopController(mSubsystemsManager, mDriver, mCodriver)
    }

    override fun robotInit() {
    }

    override fun robotPeriodic() {
    }

    override fun disabledInit() {
        // mDriver.setRumble(RumbleType.kLeftRumble, 0.0)
    }

    override fun disabledPeriodic() {
    }

    override fun autonomousInit() {
        mSandstormController.reset()
        mSandstormController.start()
    }

    override fun autonomousPeriodic() {
        mSandstormController.update()
    }

    override fun teleopInit() {
        // @Suppress("MagicNumber")
        // mDriver.setRumble(RumbleType.kLeftRumble, 1.0)
        // mDriver.rumbleForSeconds(5.0, 1.0, XboxControllerPlus.RumbleSide.BOTH)
        // mDriver.rumbleForSeconds(5.0, 1.0, XboxControllerPlus.RumbleSide.BOTH)
        mTeleopController.reset()
        mTeleopController.start()
    }

    override fun teleopPeriodic() {
        mTeleopController.update()
    }
}
