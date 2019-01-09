package org.team5499.frc2019

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.XboxController

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.subsystems.Lift
import org.team5499.frc2019.subsystems.Intake
import org.team5499.frc2019.subsystems.Vision
import org.team5499.frc2019.controllers.SandstormController
import org.team5499.frc2019.controllers.TeleopController

class Robot : TimedRobot(Constants.ROBOT_UPDATE_PERIOD) {

    // inputs
    private val mDriver: XboxControllerPlus
    private val mCodriver: XboxControllerPlus

    // subsystems
    private val mDrivetrain: Drivetrain
    private val mLift: Lift
    private val mIntake: Intake
    private val mVision: Vision
    private val mSubsystemsManager: SubsystemsManager

    // controllers
    private val mAutoController: AutoController
    private val mTeleopController: TeleopController

    init {
        mDriver = XboxControllerPlus(Constants.Input.DRIVER_PORT)
        mCodriver = XboxControllerPlus(Constants.Input.CODRIVER_PORT)

        mDrivetrain = Drivetrain()
        mLift = Lift()
        mIntake = Intake()
        mVision = Vision()
        mSubsystemsManager = SubsystemsManager(mDrivetrain, mLift, mIntake, mVision)

        mAutoController = AutoController(mSubsystemsManager, mDriver, mCodriver)
        mTeleopController = TeleopController(mSubsystemsManager, mDriver, mCodriver)
    }

    override fun robotInit() {
    }

    override fun robotPeriodic() {
    }

    override fun disabledInit() {
    }

    override fun disabledPeriodic() {
    }

   override fun autonomousInit(){
        println("Starting anutonomous")
    }
    override fun autonomousPeriodic(){
        if(controller1.anyButtonDown){
            manuallOverride = true
        }
        if(!manuallOverride){
            //mAutoController.handle()
            println("run autonomous")
        }
        else{
            //mTeleopController.handle()
            println("run teleop")
        }
    }

    override fun teleopInit() {
    }

    override fun teleopPeriodic() {
    }
}
