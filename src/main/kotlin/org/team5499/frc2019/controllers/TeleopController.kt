package org.team5499.frc2019.controllers

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.input.ControlBoard
import org.team5499.frc2019.subsystems.Lift.ElevatorHeight
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

    init {
        mSubsystems = subsystems
        mControlBoard = controlBoard
        mDriveHelper = driveHelper
    }

    public override fun start() {
        mSubsystems.drivetrain.brakeMode = false
    }

    @Suppress("ComplexMethod")
    public override fun update() {
        val driveSignal = mDriveHelper.calculateOutput(
            -mControlBoard.driverControls.getThrottle(),
            mControlBoard.driverControls.getTurn(),
            mControlBoard.driverControls.getQuickTurn()
        )
        mSubsystems.drivetrain.setPercent(driveSignal)

        if (mControlBoard.codriverControls.getExaust()) {
            mSubsystems.intake.outtake()
        } else if (mControlBoard.codriverControls.getIntake()) {
            mSubsystems.intake.intake()
        } else {
            mSubsystems.intake.hold()
        }

        val hatchPickup = mControlBoard.codriverControls.getPickup()
        if (hatchPickup.down) {
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.DEPLOYED)
        } else if (hatchPickup.released) {
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.HOLD)
        } else if (mControlBoard.codriverControls.getPlace().pressed) {
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
        }

        val manualElevatorInput = mControlBoard.codriverControls.getManualInput()
        if (Math.abs(manualElevatorInput) > Constants.Input.MANUAL_CONTROL_DEADBAND) {
            mSubsystems.lift.setPower(manualElevatorInput)
        } else if (mControlBoard.codriverControls.getHatchLow()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.HATCH_LOW)
            // mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.DEPLOYED)
        } else if (mControlBoard.codriverControls.getHatchMid()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.HATCH_MID)
            // mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.HOLD)
        } else if (mControlBoard.codriverControls.getHatchHigh()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.HATCH_HIGH)
            // mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.HOLD)
        } else if (mControlBoard.codriverControls.getBallLow()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.BALL_LOW)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
        } else if (mControlBoard.codriverControls.getBallMid()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.BALL_MID)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
        } else if (mControlBoard.codriverControls.getBallHigh()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.BALL_HIGH)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
        } else if (mControlBoard.codriverControls.getStowElevator()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.BOTTOM)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
        } else if (mControlBoard.codriverControls.getBallHumanPlayer()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.BALL_HUMAN_PLAYER)
            mSubsystems.hatchMech.setPosition(HatchMech.HatchMechPosition.BOTTOM_STOW)
        }
    }

    public override fun reset() {}
}
