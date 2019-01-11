package org.team5499.frc2019.controllers

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain
import edu.wpi.first.wpilibj.GenericHID.Hand
import org.team5499.monkeyLib.Controller
import edu.wpi.first.wpilibj.XboxController
import org.team5499.monkeyLib.input.TankDriveHelper
import org.team5499.monkeyLib.input.DriveSignal

fun XboxController.anyButtonPressed(): Boolean {
    return (
        this.getAButtonPressed() ||
        this.getBButtonPressed() ||
        this.getYButtonPressed() ||
        this.getXButtonPressed() ||
        this.getBumperPressed(Hand.kLeft) ||
        this.getBumperPressed(Hand.kRight)
    )
}

public class SandstormController(
    subsystems: SubsystemsManager,
    driver: XboxController,
    codriver: XboxController
) : Controller() {

    var manuallOverride: Boolean = false
    var driveSignal: DriveSignal
    val mDriver: XboxController
    val mCodriver: XboxController
    val mSubsystems: SubsystemsManager

    val driveHelper: TankDriveHelper = TankDriveHelper(0.06, 0.2)

    init{
        mDriver = driver
        mCodriver = codriver
        driveSignal = driveHelper.calculateOutput(mDriver.getY(Hand.kLeft), mDriver.getY(Hand.kRight), false)
        mSubsystems = subsystems
    }

    public override fun start() {
    }

    public override fun update() {
        if(mDriver.anyButtonPressed()){
            manuallOverride = true
        }
        if(!manuallOverride){
            //Auto
            mSubsystems.drivetrain.setPercent(0.2, 0.2)
        }
        else{
            //teleop
            driveSignal = driveHelper.calculateOutput(mDriver.getY(Hand.kLeft), mDriver.getY(Hand.kRight), false)
            mSubsystems.drivetrain.setPercent(driveSignal.left, driveSignal.right)
        }
    }

    public override fun reset() {
    }
}
