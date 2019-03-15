package org.team5499.frc2019.auto.actions

import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.auto.Action

import kotlin.math.abs
import org.tinylog.Logger

/**
 * An action that will make the robot drive a certian amout of inches
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param degrees The amount of inches to drive
 * @param drivetrain The drivetrain to act on
 */
public class DriveStraightAction(
    timeoutSeconds: Double,
    val distance: Double,
    val drivetrain: Drivetrain
) : Action(timeoutSeconds) {

    // Is the drivetrain currently in a turn (used to deturmine if we need to abort in finish())
    private var mIsDriving: Boolean = false

    // Called when the action starts
    public override fun start() {
        Logger.tag("ACTIONS").info("Starting drive straight action!" as Any)
        super.start()

        drivetrain.setPosition(distance)
        mIsDriving = true
    }

    public override fun next(): Boolean {
        mIsDriving = ((Constants.Drivetrain.ACCEPTABLE_DISTANCE_ERROR > abs(drivetrain.positionError)) &&
            (Constants.Drivetrain.ACCEPTABLE_VELOCITY_THRESHOLD > abs(drivetrain.averageVelocity)))

        // Return true if super.next() is true or
        // the distance error is less than the acceptable distance error/velocity defined in PID.
        return (super.next() || !mIsDriving)
    }

    public override fun finish() {
        // abort the drive if the action is aborted
        if (mIsDriving) {
            drivetrain.setPercent(0.0, 0.0)
        }
    }
}
