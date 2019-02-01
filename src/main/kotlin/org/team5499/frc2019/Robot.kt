package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.Joystick

import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.hardware.LazyVictorSPX
import org.team5499.monkeyLib.input.SpaceDriveHelper

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.subsystems.Lift
import org.team5499.frc2019.subsystems.Intake
import org.team5499.frc2019.subsystems.Vision
import org.team5499.frc2019.controllers.SandstormController
import org.team5499.frc2019.controllers.TeleopController
import org.team5499.frc2019.controllers.AutoController
import org.team5499.frc2019.input.ControlBoard
import org.team5499.frc2019.input.IDriverControls
import org.team5499.frc2019.input.ICodriverControls
import org.team5499.frc2019.input.XboxDriver
import org.team5499.frc2019.input.ButtonBoardCodriver

import com.ctre.phoenix.sensors.PigeonIMU

class Robot : TimedRobot(Constants.ROBOT_UPDATE_PERIOD) {
    // inputs
    private val mDriver: XboxController
    private val mCodriverButtonBoard: Joystick
    private val mCodriverJoystick: Joystick

    private val mSpaceDriveHelper: SpaceDriveHelper

    private val mDriverControls: IDriverControls
    private val mCodriverControls: ICodriverControls
    private val mControlBoard: ControlBoard

    // hardware

    private val mLeftMaster: LazyTalonSRX
    private val mLeftSlave1: LazyVictorSPX
    private val mLeftSlave2: LazyVictorSPX

    private val mRightMaster: LazyTalonSRX
    private val mRightSlave1: LazyVictorSPX
    private val mRightSlave2: LazyVictorSPX

    private val mGyro: PigeonIMU

    private val mLiftMaster: LazyTalonSRX
    private val mLiftSlave: LazyTalonSRX

    private val mIntakeTalon: LazyTalonSRX

    // subsystems
    private val mDrivetrain: Drivetrain
    private val mLift: Lift
    private val mIntake: Intake
    private val mVision: Vision
    private val mSubsystemsManager: SubsystemsManager

    // controllers
    private val mSandstormController: SandstormController
    private val mTeleopController: TeleopController
    private val mAutoController: AutoController

    init {
        // inputs init
        mDriver = XboxController(Constants.Input.DRIVER_PORT)
        mCodriverButtonBoard = Joystick(Constants.Input.CODRIVER_BUTTON_BOARD_PORT)
        mCodriverJoystick = Joystick(Constants.Input.CODRIVER_JOYSTICK_PORT)

        mDriverControls = XboxDriver(mDriver)
        mCodriverControls = ButtonBoardCodriver(mCodriverButtonBoard, mCodriverJoystick)
        mControlBoard = ControlBoard(mDriverControls, mCodriverControls)

        mSpaceDriveHelper = SpaceDriveHelper(Constants.Input.JOYSTICK_DEADBAND, Constants.Input.TURN_MULT)

        // hardware init
        mLeftMaster = LazyTalonSRX(Constants.HardwarePorts.LEFT_DRIVE_MASTER)
        mLeftSlave1 = LazyVictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE1)
        mLeftSlave2 = LazyVictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE2)

        mRightMaster = LazyTalonSRX(Constants.HardwarePorts.LEFT_DRIVE_MASTER)
        mRightSlave1 = LazyVictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE1)
        mRightSlave2 = LazyVictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE2)

        mGyro = PigeonIMU(Constants.HardwarePorts.GYRO_PORT)

        mLiftMaster = LazyTalonSRX(Constants.HardwarePorts.LIFT_MASTER)
        mLiftSlave = LazyTalonSRX(Constants.HardwarePorts.LIFT_SLAVE)

        mIntakeTalon = LazyTalonSRX(Constants.HardwarePorts.INTAKE)

        // reset hardware
        mLeftMaster.configFactoryDefault()
        mLeftSlave1.configFactoryDefault()
        mLeftSlave2.configFactoryDefault()

        mRightMaster.configFactoryDefault()
        mRightSlave1.configFactoryDefault()
        mRightSlave2.configFactoryDefault()

        mGyro.configFactoryDefault()

        mLiftMaster.configFactoryDefault()
        mLiftSlave.configFactoryDefault()

        mIntakeTalon.configFactoryDefault()

        // subsystem init
        mDrivetrain = Drivetrain(
            mLeftMaster, mLeftSlave1, mLeftSlave2,
            mRightMaster, mRightSlave1, mRightSlave2,
            mGyro
        )
        mLift = Lift(mLiftMaster, mLiftSlave)
        mIntake = Intake(mIntakeTalon)
        mVision = Vision()
        mSubsystemsManager = SubsystemsManager(mDrivetrain, mLift, mIntake, mVision)

        // controllers init
        mTeleopController = TeleopController(mSubsystemsManager, mControlBoard, mSpaceDriveHelper)
        mAutoController = AutoController(mSubsystemsManager)
        mSandstormController = SandstormController(mControlBoard, mTeleopController, mAutoController)
    }

    override fun robotInit() {
    }

    override fun robotPeriodic() {
    }

    override fun disabledInit() {
        mSubsystemsManager.resetAll()
    }

    override fun disabledPeriodic() {
    }

    override fun autonomousInit() {
        mSandstormController.reset()
        mSandstormController.start()
    }

    override fun autonomousPeriodic() {
        mSandstormController.update()
        mSubsystemsManager.updateAll()
    }

    override fun teleopInit() {
        mTeleopController.reset()
        mTeleopController.start()
    }

    override fun teleopPeriodic() {
        mTeleopController.update()
        mSubsystemsManager.updateAll()
    }
}
