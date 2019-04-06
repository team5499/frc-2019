package org.team5499.frc2019.controllers

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.input.ControlBoard
import org.team5499.frc2019.subsystems.Lift.LiftHeight
import org.team5499.frc2019.subsystems.HatchMech
import org.team5499.frc2019.subsystems.Vision.VisionMode
import org.team5499.frc2019.subsystems.Vision.LEDState

import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.input.DriveHelper
import org.team5499.monkeyLib.math.pid.PIDF

@Suppress("LargeClass")
public class TeleopController(
    subsystems: SubsystemsManager,
    controlBoard: ControlBoard,
    driveHelper: DriveHelper
)
: Controller() {

    private val mControlBoard: ControlBoard

    private val mSubsystems: SubsystemsManager

    private val mDriveHelper: DriveHelper

    private val mDistancePID: PIDF
    private val mAnglePID: PIDF

    private var mLockHatchMech: Boolean
    private var mLockElevator: Boolean
    private var mLastLoopManualUsed: Boolean
    private var mAutoAlign: Boolean

    private var mStarted: Boolean

    init {
        mSubsystems = subsystems
        mControlBoard = controlBoard
        mDriveHelper = driveHelper
        mLockHatchMech = false
        mLockElevator = false
        mLastLoopManualUsed = false
        mStarted = false
        mAutoAlign = false

        mAnglePID = PIDF(
            Constants.Vision.ANGLE_KP,
            Constants.Vision.ANGLE_KI,
            Constants.Vision.ANGLE_KI,
            Constants.Vision.ANGLE_KD,
            false
        )
        mDistancePID = PIDF(
            Constants.Vision.DISTANCE_KP,
            Constants.Vision.DISTANCE_KI,
            Constants.Vision.DISTANCE_KD,
            Constants.Vision.DISTANCE_KF,
            false
        )
    }

    public override fun start() {
        if (!mStarted) {
            mSubsystems.drivetrain.brakeMode = false
            mLockHatchMech = false
            mLockElevator = false
            mStarted = true
            mSubsystems.vision.ledState = LEDState.OFF
            mSubsystems.vision.visionMode = VisionMode.VISION
            // mSubsystems.lift.setPositionRaw(mSubsystems.lift.setpoint.toInt())

            mAnglePID.reset()
            mDistancePID.reset()

            mAnglePID.kP = Constants.Vision.ANGLE_KP
            mAnglePID.kI = Constants.Vision.ANGLE_KI
            mAnglePID.kD = Constants.Vision.ANGLE_KD
            mAnglePID.kF = Constants.Vision.ANGLE_KF

            mDistancePID.kP = Constants.Vision.DISTANCE_KP
            mDistancePID.kI = Constants.Vision.DISTANCE_KI
            mDistancePID.kD = Constants.Vision.DISTANCE_KD
            mDistancePID.kF = Constants.Vision.DISTANCE_KF
        }
    }

    @Suppress("ComplexMethod")
    public override fun update() {
        val isAutoAlign = mControlBoard.driverControls.getAutoAlign()
        if (!mAutoAlign && isAutoAlign) {
            mAutoAlign = true
            mDistancePID.reset()
            mAnglePID.reset()
            mDistancePID.setpoint = Constants.Vision.TARGET_DISTANCE
            mAnglePID.setpoint = -Constants.Vision.CAMERA_HORIZONTAL_ANGLE
            mSubsystems.vision.ledState = LEDState.ON
            mSubsystems.vision.visionMode = VisionMode.VISION
        } else if (mAutoAlign && !isAutoAlign) {
            mAutoAlign = false
            mSubsystems.vision.ledState = LEDState.OFF
            mSubsystems.vision.visionMode = VisionMode.DRIVER
        }

        if (!mAutoAlign) {
            // driver can control
            val driveSignal = mDriveHelper.calculateOutput(
                -mControlBoard.driverControls.getThrottle(),
                mControlBoard.driverControls.getTurn(),
                mControlBoard.driverControls.getQuickTurn(),
                mControlBoard.driverControls.getCreep()
            )
            mSubsystems.drivetrain.setPercent(driveSignal.left, driveSignal.right)
        } else {
            // vision system can control
            if (mSubsystems.vision.hasValidTarget) {
                // mSubsystems.vision.ledState = LEDState.ON

                mAnglePID.processVariable = -mSubsystems.vision.targetXOffset
                mDistancePID.processVariable = -mSubsystems.vision.distanceToTarget

                val drive = mDistancePID.calculate()
                val steer = mAnglePID.calculate()
                val left = drive + steer
                val right = drive - steer
                mSubsystems.drivetrain.setVelocity(left, right)
            } else {
                // if not target, stop and blink
                mSubsystems.drivetrain.setVelocity(0.0, 0.0)
                // mSubsystems.vision.ledState = LEDState.BLINK
            }
        }

        if (mControlBoard.codriverControls.getExaust()) {
            mSubsystems.intake.outtake()
        } else if (mControlBoard.codriverControls.getIntake()) {
            mSubsystems.intake.intake()
        } else {
            mSubsystems.intake.hold()
        }

        if (super.timer.get() > Constants.Input.DRIVER_STOW_TIMEOUT) {
            mLockElevator = false
            super.timer.stop()
            super.timer.reset()
        } else if (mControlBoard.driverControls.getStow()) {
            super.timer.start()
            mSubsystems.lift.setIntakeHeight(LiftHeight.BOTTOM)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
            mLockElevator = true
            mLockHatchMech = true
        }

        if (!mLockHatchMech) {
            val hatchPickup = mControlBoard.codriverControls.getPickup()
            if (hatchPickup.down) {
                mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.DEPLOYED)
            } else if (hatchPickup.released) {
                mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.HOLD)
            } else if (mControlBoard.codriverControls.getPlace().pressed) {
                mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
            }
        }

        val manualElevatorInput = mControlBoard.codriverControls.getManualInput()
        if (mLockElevator) {
            // do nothing
        } else if (mControlBoard.codriverControls.getManualZero()) {
            mSubsystems.lift.zeroed = false
        } else if (mControlBoard.codriverControls.getManualEnable() &&
            Math.abs(manualElevatorInput) > Constants.Input.MANUAL_CONTROL_DEADBAND) {
            mSubsystems.lift.setPower(manualElevatorInput)
            mLockHatchMech = false
            mLastLoopManualUsed = true
        } else if (mLastLoopManualUsed) {
            mSubsystems.lift.setPower(0.0)
            mLastLoopManualUsed = false
        } else if (mControlBoard.codriverControls.getHatchLow()) {
            mSubsystems.lift.setIntakeHeight(LiftHeight.HATCH_LOW)
            mLockHatchMech = false
        } else if (mControlBoard.codriverControls.getHatchMid()) {
            mSubsystems.lift.setIntakeHeight(LiftHeight.HATCH_MID)
            mLockHatchMech = false
        } else if (mControlBoard.codriverControls.getHatchHigh()) {
            mSubsystems.lift.setIntakeHeight(LiftHeight.HATCH_HIGH)
            mLockHatchMech = false
        } else if (mControlBoard.codriverControls.getBallLow()) {
            mSubsystems.lift.setIntakeHeight(LiftHeight.BALL_LOW)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
            mLockHatchMech = true
        } else if (mControlBoard.codriverControls.getBallMid()) {
            mSubsystems.lift.setIntakeHeight(LiftHeight.BALL_MID)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
            mLockHatchMech = true
        } else if (mControlBoard.codriverControls.getBallHigh()) {
            mSubsystems.lift.setIntakeHeight(LiftHeight.BALL_HIGH)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
            mLockHatchMech = true
        } else if (mControlBoard.codriverControls.getStowElevator()) {
            mSubsystems.lift.setIntakeHeight(LiftHeight.BOTTOM)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
            mLockHatchMech = true
        } else if (mControlBoard.codriverControls.getBallHumanPlayer()) {
            mSubsystems.lift.setIntakeHeight(LiftHeight.BALL_HUMAN_PLAYER)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
            mLockHatchMech = true
        }
    }

    public override fun reset() {
        mLockHatchMech = false
        mLockElevator = false
        mStarted = false
        mAutoAlign = false
    }
}
