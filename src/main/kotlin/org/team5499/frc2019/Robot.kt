package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.XboxController

import org.team5499.monkeyLib.hardware.LazyTalonSRX

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.subsystems.Lift
import org.team5499.frc2019.subsystems.Intake
import org.team5499.frc2019.subsystems.Vision
import org.team5499.frc2019.controllers.SandstormController
import org.team5499.frc2019.controllers.TeleopController

class Robot : TimedRobot(Constants.ROBOT_UPDATE_PERIOD) {

    // inputs
    private val mDriver: XboxController
    private val mCodriver: XboxController

    // hardware
    private val mLiftMaster: LazyTalonSRX
    private val mLiftSlave: LazyTalonSRX

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
        // init inputs
        mDriver = XboxController(Constants.Input.DRIVER_PORT)
        mCodriver = XboxController(Constants.Input.CODRIVER_PORT)

        // init hardware
        mLiftMaster = LazyTalonSRX(Constants.HardwarePorts.LIFT_MASTER)
        mLiftSlave = LazyTalonSRX(Constants.HardwarePorts.LIFT_SLAVE)

        // init subsystems
        mDrivetrain = Drivetrain()
        mLift = Lift(mLiftMaster, mLiftSlave)
        mIntake = Intake()
        mVision = Vision()
        mSubsystemsManager = SubsystemsManager(mDrivetrain, mLift, mIntake, mVision)

        // init controllers
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
        mSandstormController.reset()
        mSandstormController.start()
    }

    override fun autonomousPeriodic() {
        mSandstormController.update()
    }

    override fun teleopInit() {
        mTeleopController.reset()
        mTeleopController.start()
    }

    override fun teleopPeriodic() {
        mTeleopController.update()
    }
}
