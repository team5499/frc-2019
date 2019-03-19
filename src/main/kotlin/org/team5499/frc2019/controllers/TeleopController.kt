package org.team5499.frc2019.controllers

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.input.ControlBoard
import org.team5499.frc2019.subsystems.Lift.LiftHeight
import org.team5499.frc2019.subsystems.HatchMech
import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.input.DriveHelper

public class TeleopController(
    subsystems: SubsystemsManager,
    controlBoard: ControlBoard,
    driveHelper: DriveHelper
)
: Controller() {

    private val mControlBoard: ControlBoard

    private val mSubsystems: SubsystemsManager

    private val mDriveHelper: DriveHelper

    private var mLockHatchMech: Boolean
    private var mLockElevator: Boolean
    private var mLastLoopManualUsed: Boolean

    private var started = false

    init {
        mSubsystems = subsystems
        mControlBoard = controlBoard
        mDriveHelper = driveHelper
        mLockHatchMech = false
        mLockElevator = false
        mLastLoopManualUsed = false
    }

    public override fun start() {
        if (!started) {
            mSubsystems.drivetrain.brakeMode = false
            mLockHatchMech = false
            mLockElevator = false
            started = true
        }
    }

    @Suppress("ComplexMethod")
    public override fun update() {
        val driveSignal = mDriveHelper.calculateOutput(
            -mControlBoard.driverControls.getThrottle(),
            mControlBoard.driverControls.getTurn(),
            mControlBoard.driverControls.getQuickTurn(),
            mControlBoard.driverControls.getCreep()
        )
        mSubsystems.drivetrain.setPercent(driveSignal.left, driveSignal.right)

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
        started = false
    }
}
