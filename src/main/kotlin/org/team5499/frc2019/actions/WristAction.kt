package org.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.Wrist
import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.auto.Action

import kotlin.math.abs

/**
 * An action that sets the angle of the wrist on the elevator.
 *
 * @param timeoutSeconds The number of seconds to wait before canceling the command
 * @param angle The angle to set the wrist to
 * @param wrist The wrist to act on
 */
public class WristAction(
    timeoutSeconds: Double,
    val angle: Double,
    val wrist: Wrist
) : Action(timeoutSeconds) {

    // Is the wrist currently moving (used to deturmine if we need to abort in finish())
    private var mIsMoving: Boolean = false

    public override fun start() {
        wrist.setAngle(angle)
    }

    // Called every tick
    public override fun update() {
        mIsMoving = ((Constants.PID.ACCEPTABLE_TURN_ERROR > abs(wrist.angleError)) &&
            (Constants.PID.ACCEPTABLE_VELOCITY_THRESHOLD > abs(wrist.velocity)))
    }

    // Returns true if we are ready to move on to the next action
    public override fun next(): Boolean {
        return (super.next() || !mIsMoving)
    }

    public override fun finish() {
        super.finish()

        if (mIsMoving) {
            wrist.setPower(0.0)
        }
    }
}
