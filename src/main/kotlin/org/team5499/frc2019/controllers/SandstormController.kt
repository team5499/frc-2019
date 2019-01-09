package org.team5499.frc2019.controllers
import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.driveTrain

import edu.wpi.first.wpilibj.GenericHID.Hand

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.Input.XboxControllerPlus
import org.team5499.monkeyLib.Input.TankDriverHelper
import org.team5499.monkeyLib.Input.DriveSignal

public class Sandstorm(
    subsystems: SubsystemsManager,
    driver: XboxControllerPlus,
    codriver: XboxControllerPlus
) : Controller() {
    var manuallOverride: Boolean = false
    var driveSignal: DriveSignal

    public override fun start() {
        driveHelper: TankDriverHelper = TankDriverHelper(0.06, 0.2)
    }

    public override fun update() {
        if(driver.anyButtonPressed()){
            manuallOverride = true
        }
        if(!manuallOverride){
            //Auto
            //TODO

        }
        else{
            //teleop
            //TOTEST
            driveSignal = driveHelper.calculateOutput(driver.getX(kLeft), driver.getX(kRight))
            driveTrain.setPercent(driveSignal.left, driveSignal.right)
        }
    }

    public override fun reset() {
    }
}
