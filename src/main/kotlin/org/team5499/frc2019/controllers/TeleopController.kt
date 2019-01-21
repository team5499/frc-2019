package org.team5499.frc2019.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import org.team5499.frc2019.subsystems.SubsystemsManager

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.input.DriveHelper

public class TeleopController(
    subsystems: SubsystemsManager,
    driver: XboxController,
    codriver: XboxController,
    driveHelper: DriveHelper
)
: Controller() {

    private val mDriver: XboxController
    private val mCodriver: XboxController

    private val mSubsystems: SubsystemsManager

    private val mDriveHelper: DriveHelper

    init {
        mSubsystems = subsystems
        mDriver = driver
        mCodriver = codriver
        mDriveHelper = driveHelper
    }

    public override fun start() {
        mSubsystems.drivetrain.brakeMode = false
    }

    public override fun update() {
        val driveSignal = mDriveHelper.calculateOutput(
            mDriver.getY(Hand.kLeft),
            mDriver.getX(Hand.kRight),
            mDriver.getBumper(Hand.kRight)
        )
        mSubsystems.drivetrain.setPercent(driveSignal)
    }

    public override fun reset() {}
}
