package org.team5499.frc2019.controllers

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import org.team5499.frc2019.subsystems.SubsystemsManager

import org.team5499.monkeyLib.hardware.LazyTalonSRX

import org.team5499.monkeyLib.Controller

import com.ctre.phoenix.motorcontrol.ControlMode

public class TeleopController(
    subsystems: SubsystemsManager,
    driver: XboxController,
    codriver: XboxController
) : Controller() {

    private val mDriver: XboxController
    private val mCodriver: XboxController
    private val mSubsystems: SubsystemsManager

    init {
        mSubsystems = subsystems
        mDriver = driver
        mCodriver = codriver
    }

    @Suppress("MagicNumber")
    public override fun start() {
        elevPos = 50
    }

    private var elevPos = 0

    @Suppress("MagicNumber")
    private val mTalon = LazyTalonSRX(9)

    @Suppress("MagicNumber", "ComplexMethod")
    public override fun update() {
        mSubsystems.drivetrain.setPercent(mDriver.getY(Hand.kRight), mDriver.getY(Hand.kLeft))
        if (mDriver.getAButtonPressed()) elevPos = 100
        else if (mDriver.getBButtonPressed()) elevPos = 1500
        else if (mDriver.getXButtonPressed()) elevPos = 3000
        else if (mDriver.getYButtonPressed()) elevPos = 4500
        else if (mDriver.getBumperPressed(Hand.kLeft)) elevPos = 6000
        else if (mDriver.getBumperPressed(Hand.kRight)) elevPos = 8000

        if (mDriver.getTriggerAxis(Hand.kLeft) > 0.5) mTalon.set(ControlMode.PercentOutput, 1.0)
        else if (mDriver.getTriggerAxis(Hand.kRight) > 0.5) mTalon.set(ControlMode.PercentOutput, -1.0)
        else mTalon.set(ControlMode.PercentOutput, 0.0)

        mSubsystems.lift.setPositionRaw(elevPos)
    }

    public override fun reset() {
    }
}
