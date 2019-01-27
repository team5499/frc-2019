package org.team5499.frc2019.controllers

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.input.ControlBoard
import org.team5499.frc2019.subsystems.Intake.IntakeMode

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
            mControlBoard.driverControls.getThrottle(),
            mControlBoard.driverControls.getTurn(),
            mControlBoard.driverControls.getQuickTurn()
        )
        mSubsystems.drivetrain.setPercent(driveSignal)

        if (mControlBoard.codriverControls.getIntake()) {
            mSubsystems.intake.setDesiredIntakeMode(IntakeMode.INTAKE)
        } else if (mControlBoard.codriverControls.getExaust()) {
            mSubsystems.intake.setDesiredIntakeMode(IntakeMode.OUTTAKE)
        } else {
            mSubsystems.intake.setDesiredIntakeMode(IntakeMode.IDLE)
        }
    }

    public override fun reset() {}
}
