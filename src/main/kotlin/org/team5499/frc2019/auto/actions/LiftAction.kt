package org.team5499.frc2019.auto.actions

import org.team5499.frc2019.subsystems.Lift
import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.auto.Action
import kotlin.math.abs

/**
 * An action that sets the elevator to a certain height.
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param height The height to go to
 * @param lift The lift to act on
 */
public class LiftAction(
    timeoutSeconds: Double,
    val heightInches: Double,
    val lift: Lift
) : Action(timeoutSeconds) {

    // Is the lift still moving?
    private var mIsMoving = true

    public override fun start() {
        lift.setPosition(heightInches)
    }

    // Called every tick
    public override fun update() {
        // calculate if we're still moving
        mIsMoving = ((lift.secondStageVelocityInchesPerSecond >= Constants.Lift.ACCEPTABLE_VELOCITY_THRESHOLD) ||
            (abs(lift.secondStagePositionErrorInches) > (Constants.Lift.ACCEPTABLE_DISTANCE_ERROR)))
    }

    // Returns true if we are ready to move on to the next action
    public override fun next(): Boolean {
        return (super.next() || !mIsMoving)
    }

    public override fun finish() {
        // stop moving if we still are still moving
        if (mIsMoving) {
            lift.setPower(0.0)
        }
    }
}
