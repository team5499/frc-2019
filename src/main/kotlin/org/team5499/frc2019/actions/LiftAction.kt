package org.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Lift

import org.team5499.monkeyLib.auto.Action
import kotlin.math.abs

/**
 * An action that sets the elevator to a certain height.
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param liftHeight The height to go to
 * @param subsystemsManager The subsystems manager to get the drivetrain from
 */
public class LiftAction(
    timeoutseconds: Double,
    val height: Double,
    subsystemsManager: SubsystemsManager
) : Action(timeoutseconds) {

    // Keep a reference to the drivetrain locally for simplicity's sake
    private val mLift: Lift = subsystemsManager.lift
    // Is the lift still moving?
    private var mIsMoving = true


    public override fun start() {
        mLift.setPosition(liftHeight)
    }

    // Called every tick
    public override fun update() {
        // calculate if we're still moving
        bIsMoving = ((mLift.velocity >= Constants.PID.ACCEPTABLE_VELOCITY_THRESHOLD_LIFT) ||
         (abs(mLift.positionError) > (Constants.PID.ACCEPTABLE_DISTANCE_ERROR_LIFT))

    }

    // Returns true if we are ready to move on to the next action
    public override fun next(): Boolean {
        return (super.next() || !bIsMoving)
    }

    public override fun finish() {
        // stop moving if we still are still moving
        if (mIsMoving){
            mLift.setPower(0)
        }
    }
}
