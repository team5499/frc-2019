package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.PowerDistributionPanel

import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.hardware.LazyVictorSPX
import org.team5499.monkeyLib.input.SpaceDriveHelper
import org.team5499.monkeyLib.path.PathGenerator

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.subsystems.Lift
import org.team5499.frc2019.subsystems.Intake
import org.team5499.frc2019.subsystems.Vision
import org.team5499.frc2019.subsystems.Vision.LEDState
import org.team5499.frc2019.subsystems.Vision.VisionMode
import org.team5499.frc2019.subsystems.HatchMech
import org.team5499.frc2019.controllers.SandstormController
import org.team5499.frc2019.controllers.TeleopController
import org.team5499.frc2019.controllers.AutoController
import org.team5499.frc2019.input.ControlBoard
import org.team5499.frc2019.input.IDriverControls
import org.team5499.frc2019.input.ICodriverControls
import org.team5499.frc2019.input.XboxDriver
import org.team5499.frc2019.input.ButtonBoardCodriver
import org.team5499.frc2019.auto.Paths
import org.team5499.frc2019.auto.Routines

import org.team5499.dashboard.Dashboard

import com.ctre.phoenix.sensors.PigeonIMU

import org.tinylog.Logger

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

    private val mHatchMechTalon: LazyTalonSRX

    private val mPdp: PowerDistributionPanel

    // subsystems
    private val mDrivetrain: Drivetrain
    private val mLift: Lift
    private val mIntake: Intake
    private val mHatchMech: HatchMech
    private val mVision: Vision
    private val mSubsystemsManager: SubsystemsManager

    // path
    private val mPathGenerator: PathGenerator
    private val mPaths: Paths
    private val mRoutines: Routines

    // controllers
    private val mSandstormController: SandstormController
    private val mTeleopController: TeleopController
    private val mAutoController: AutoController

    init {
        // init dashboard
        Dashboard.start(this, "dashConfig.json")
        Constants.initConstants()

        // inputs init
        mDriver = XboxController(Constants.Input.DRIVER_PORT)
        mCodriverButtonBoard = Joystick(Constants.Input.CODRIVER_BUTTON_BOARD_PORT)
        mCodriverJoystick = Joystick(Constants.Input.CODRIVER_JOYSTICK_PORT)

        mDriverControls = XboxDriver(mDriver)
        mCodriverControls = ButtonBoardCodriver(mCodriverButtonBoard, mCodriverJoystick)
        mControlBoard = ControlBoard(mDriverControls, mCodriverControls)

        mSpaceDriveHelper = SpaceDriveHelper(
            { Constants.Input.JOYSTICK_DEADBAND },
            { Constants.Input.TURN_MULT },
            { Constants.Input.SLOW_MULT }
        )

        // hardware init
        mLeftMaster = LazyTalonSRX(Constants.Drivetrain.LEFT_MASTER_TALON_PORT)
        mLeftSlave1 = LazyVictorSPX(Constants.Drivetrain.LEFT_SLAVE1_TALON_PORT)
        mLeftSlave2 = LazyVictorSPX(Constants.Drivetrain.LEFT_SLAVE2_TALON_PORT)

        mRightMaster = LazyTalonSRX(Constants.Drivetrain.RIGHT_MASTER_TALON_PORT)
        mRightSlave1 = LazyVictorSPX(Constants.Drivetrain.RIGHT_SLAVE1_TALON_PORT)
        mRightSlave2 = LazyVictorSPX(Constants.Drivetrain.RIGHT_SLAVE2_TALON_PORT)

        mGyro = PigeonIMU(Constants.Drivetrain.GYRO_PORT)

        mLiftMaster = LazyTalonSRX(Constants.Lift.MASTER_TALON_PORT)
        mLiftSlave = LazyTalonSRX(Constants.Lift.SLAVE_TALON_PORT)

        mIntakeTalon = LazyTalonSRX(Constants.Intake.TALON_PORT)

        mHatchMechTalon = LazyTalonSRX(Constants.Hatch.TALON_PORT)

        mPdp = PowerDistributionPanel()

        // reset hardware
        mLeftMaster.configFactoryDefault()
        mLeftSlave1.configFactoryDefault()
        mLeftSlave2.configFactoryDefault()

        mRightMaster.configFactoryDefault()
        mRightSlave1.configFactoryDefault()
        mRightSlave2.configFactoryDefault()

        mGyro.configFactoryDefault()
        mGyro.setFusedHeading(0.0)

        mLiftMaster.configFactoryDefault()
        mLiftSlave.configFactoryDefault()

        mIntakeTalon.configFactoryDefault()

        mHatchMechTalon.configFactoryDefault()

        // subsystem init
        mDrivetrain = Drivetrain(
            mLeftMaster, mLeftSlave1, mLeftSlave2,
            mRightMaster, mRightSlave1, mRightSlave2,
            mGyro
        )
        mLift = Lift(mLiftMaster, mLiftSlave)
        mIntake = Intake(mIntakeTalon)
        mHatchMech = HatchMech(mHatchMechTalon)
        mVision = Vision()
        mSubsystemsManager = SubsystemsManager(mDrivetrain, mLift, mIntake, mHatchMech, mVision)

        // path init
        @Suppress("MagicNumber")
        mPathGenerator = PathGenerator(
            { Constants.Drivetrain.MAX_VELOCITY },
            { Constants.Drivetrain.MAX_ACCELERATION },
            { 20.0 },
            { 0.0 }
        )
        mPaths = Paths(mPathGenerator)
        mRoutines = Routines(mPaths, mSubsystemsManager)

        // controllers init
        mTeleopController = TeleopController(mSubsystemsManager, mControlBoard, mSpaceDriveHelper)
        mAutoController = AutoController(mSubsystemsManager, mRoutines)
        mSandstormController = SandstormController(mControlBoard, mTeleopController, mAutoController)
    }

    override fun robotInit() {
        Logger.warn("Robot initializing" as Any)
        mVision.ledState = Vision.LEDState.OFF
        mVision.visionMode = Vision.VisionMode.VISION
    }

    override fun robotPeriodic() {
        Logging.update(mSubsystemsManager,
                        mPdp,
                        mControlBoard,
                        mLeftMaster,
                        mLeftSlave1,
                        mLeftSlave2,
                        mRightMaster,
                        mRightSlave1,
                        mRightSlave2,
                        mLiftMaster,
                        mLiftSlave,
                        mIntakeTalon,
                        mHatchMechTalon)
        Dashboard.update()
    }

    override fun disabledInit() {
        Logger.warn("Robot disabling" as Any)
        mLift.zeroed = false
        // mVision.initialize()
        mVision.ledState = Vision.LEDState.OFF
        mVision.visionMode = Vision.VisionMode.VISION
        mSubsystemsManager.resetAll()
    }

    override fun disabledPeriodic() {
        mVision.initialize()
    }

    override fun autonomousInit() {
        Logger.warn("Robot going autonomous" as Any)
        mSubsystemsManager.resetAll()
        mTeleopController.reset()
        mSandstormController.reset()
        mSandstormController.start()
    }

    override fun autonomousPeriodic() {
        mSandstormController.update()
        mSubsystemsManager.updateAll()
    }

    override fun teleopInit() {
        Logger.warn("Robot going teleoperated" as Any)
        mTeleopController.start()
    }

    override fun teleopPeriodic() {
        mTeleopController.update()
        mSubsystemsManager.updateAll()
    }

    override fun testInit() {
        mVision.ledState = LEDState.ON
        mVision.visionMode = VisionMode.VISION
    }

    override fun testPeriodic() {
        println("distance to target ${mVision.distanceToTarget}")
    }
}
