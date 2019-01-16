package org.team5499.frc2019.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import org.team5499.frc2019.subsystems.SubsystemsManager

import org.team5499.monkeyLib.Controller
import org.team5499.frc2019.subsystems.Lift

public class TeleopController(
    subsystems: SubsystemsManager,
    driver: XboxController,
    codriver: XboxController
) : Controller() {

    private val mDriver: XboxController
    private val mSubsystems: SubsystemsManager

    init {
        mSubsystems = subsystems
        mDriver = driver
    }

    public override fun start() {
    }

    public override fun update() {
        mSubsystems.drivetrain.setPercent(-mDriver.getY(Hand.kLeft), -mDriver.getY(Hand.kRight))
        if (mDriver.getAButton()) mSubsystems.lift.setPositionRaw(Lift.kMinElevatorTicks)
        if (mDriver.getBButton()) mSubsystems.lift.setPositionRaw((Lift.kMaxElevatorTicks + Lift.kMinElevatorTicks) / 2)
    }

    public override fun reset() {
    }
}
