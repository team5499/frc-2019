package org.team5499.frc2019.controllers

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.input.ControlBoard

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
            mSubsystems.intake.idle()
        }
    }

    public override fun reset() {}
}
