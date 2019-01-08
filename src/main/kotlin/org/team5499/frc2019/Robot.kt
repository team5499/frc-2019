package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot
import org.team5499.monkeyLib.input.XboxControllerPlus

class Robot : TimedRobot() {
    private val controller1 : XboxControllerPlus = XboxControllerPlus(0)
    private val controller2 : XboxControllerPlus = XboxControllerPlus(1)
    private var manuallOverride : Bool = false
    override fun robotInit() {
        println("Hello World from Kotlin!")
    }
    override fun autonomousInit(){
        println("Starting anutonomous")
    }
    override fun autonomousPeriodic(){
        if(controller1.anyButtonDown){
            manuallOverride = true
        }
        if(!manuallOverride){
            println("run autonomous")
        }
        else{
            println("run teleop")
        }
    }
}
