package org.team5499.frc2019.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.monkeyLib.Controller

public class TeleopController(
    subsystems: SubsystemsManager,
    driver: XboxController,
    codriver: XboxController
)
: Controller() {

    private val mDriver: XboxController
    private val mCodriver: XboxController

    private val mSubsystems: SubsystemsManager

    init {
        mSubsystems = subsystems
        mDriver = driver
        mCodriver = codriver
    }

    public override fun start() {
    }

    private var elevPos = 0

    public override fun update() {
        mSubsystems.drivetrain.setPercent(mDriver.getY(Hand.kRight), mDriver.getY(Hand.kLeft))
    }

    public override fun reset() {
    }
}
