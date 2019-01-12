package org.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Lift

import org.team5499.monkeyLib.auto.Action

/**
 * An action that sets the elevator to a certain height.
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param liftHeight The height to go to
 * @param subsystemsManager The subsystems manager to get the drivetrain from
 */
public class LiftAction(
    timeoutseconds: Double,
    height: Double,
    subsystemsManager: SubsystemsManager
) : Action(timeoutseconds) {

    // Keep a reference to the drivetrain locally for simplicity's sake
    private val mLift: Lift = subsystemsManager.lift

    private val liftHeight = height

    public override fun start() {
        mLift.setPosition(liftHeight)
    }

    // Called every tick
    public override fun update() {}

    // Returns true if we are ready to move on to the next action
    public override fun next(): Boolean {
        return (super.next() || mLift.positionInches == liftHeight)
    }
}
