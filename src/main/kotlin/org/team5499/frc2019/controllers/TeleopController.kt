package org.team5499.frc2019.controllers

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.input.ControlBoard

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.input.DriveHelper

import org.team5499.frc2019.subsystems.Lift.ElevatorHeight

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

        if (mControlBoard.codriverControls.getHatchLow()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.HATCH_LOW)
        } else if (mControlBoard.codriverControls.getStowElevator()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.BOTTOM)
        } else if (mControlBoard.codriverControls.getBallHumanPlayer()) {
            mSubsystems.lift.setIntakeHeight(ElevatorHeight.BALL_HUMAN_PLAYER)
        }
    }

    public override fun reset() {}
}
