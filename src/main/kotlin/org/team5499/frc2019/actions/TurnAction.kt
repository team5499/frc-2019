package org.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.math.geometry.Pose2d

import kotlin.math.abs

/**
 * An action that will turn the robot a certian amount of degrees
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param degrees The amount of degrees to turn
 * @param drivetrain The drivetrain to act on
 */
public class TurnAction(
    timeoutseconds: Double,
    degrees: Double,
    val drivetrain: Drivetrain
) : Action(timeoutseconds) {

    // Keep track of the initial pose so we know when to end the auto command
    private val mInitialPose: Pose2d = drivetrain.pose
    // Fix issue where start() cannot read degrees value
    private val degrees = degrees
    // Is the drivetrain currently in a turn (used to deturmine if we need to abort in finish())
    private var mIsTurning: Boolean = false

    // Called when the action starts
    public override fun start() {
        super.start()

        drivetrain.setTurn(degrees)
        mIsTurning = true
    }

    public override fun next(): Boolean {
        mIsTurning = ((Constants.PID.ACCEPTABLE_TURN_ERROR > abs(drivetrain.turnError)) &&
            (Constants.PID.ACCEPTABLE_VELOCITY_THRESHOLD > abs(drivetrain.angularVelocity)))

        // Return true if super.next() is true or
        // the turn error is les than the acceptable turn error/velocity defined in PID.
        return (super.next() || !mIsTurning)
    }

    public override fun finish() {
        // abort the turn if the action is aborted
        if (mIsTurning) {
            drivetrain.setPercent(0.0, 0.0)
        }
    }
}