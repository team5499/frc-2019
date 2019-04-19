package org.team5499.frc2019.auto.actions

import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.Constants

import org.tinylog.Logger
import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.pid.PIDF

/**
 * An action that will turn the robot a certian amount of degrees
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param degrees The amount of degrees to turn
 * @param drivetrain The drivetrain to act on
 */
public class TurnAction(
    timeoutSeconds: Double,
    degrees: Double,
    val drivetrain: Drivetrain
) : Action(timeoutSeconds) {

    // Keep track of the initial pose for absolute heading control
    private val mInitialPose: Pose2d = drivetrain.pose

    private var mTargetAngle: Double = 0.0
    // target angle relative to start
    private val mDegrees: Double

    private val mPID: PIDF

    init {
        mDegrees = degrees
        mPID = PIDF(
            Constants.Drivetrain.TURN_KP,
            Constants.Drivetrain.TURN_KI,
            Constants.Drivetrain.ANGLE_KD,
            0.0
        )
    }

    // Called when the action starts
    public override fun start() {
        Logger.tag("ACTIONS").info("Starting turn action!" as Any)
        super.start()

        mTargetAngle = drivetrain.heading.degrees + mDegrees
        // while(mTargetAngle >= 360) mTargetAngle -= 360.0
        // while(mTargetAngle < 0) mTargetAngle += 360.0

        mPID.setpoint = mTargetAngle
        // drivetrain.setTurn(mDegrees)
        mPID.kP = Constants.Drivetrain.TURN_KP
        mPID.kI = Constants.Drivetrain.TURN_KI
        mPID.kD = Constants.Drivetrain.TURN_KD
    }

    public override fun update() {
        // mIsTurning = ((Constants.Drivetrain.ACCEPTABLE_TURN_ERROR > abs(drivetrain.turnError)) &&
        //     (Constants.Drivetrain.ACCEPTABLE_VELOCITY_THRESHOLD > abs(drivetrain.angularVelocity)))
        mPID.processVariable = drivetrain.heading.degrees
        val output = mPID.calculate()
        val speed = output
        drivetrain.setVelocity(-speed, speed)
    }

    public override fun next(): Boolean {
        // Return true if super.next() is true or
        // the turn error is les than the acceptable turn error/velocity defined in PID.
        val turning = drivetrain.angularVelocity < 2.0
        return (super.next() || !turning)
    }

    public override fun finish() {
        // abort the turn if the action is aborted
        Logger.tag("ACTIONS").info("Finishing turn action!" as Any)
    }
}
