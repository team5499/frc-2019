package org.team5499.frc2019.controllers

import edu.wpi.first.wpilibj.GenericHID.Hand

import org.team5499.frc2019.subsystems.SubsystemsManager

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.hardware.XboxControllerPlus

public class TeleopController(
    subsystems: SubsystemsManager,
    driver: XboxControllerPlus,
    codriver: XboxControllerPlus
) : Controller() {

    private val mDriver: XboxControllerPlus
    private val mSubsystems: SubsystemsManager

    init {
        mSubsystems = subsystems
        mDriver = driver
    }

    public override fun start() {
    }

    public override fun update() {
        mSubsystems.drivetrain.setPercent(-mDriver.getY(Hand.kLeft), -mDriver.getY(Hand.kRight))
    }

    public override fun reset() {
    }
}
