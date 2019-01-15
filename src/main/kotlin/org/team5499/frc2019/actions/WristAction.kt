package org.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.Wrist

import org.team5499.monkeyLib.auto.Action

/**
 * An action that sets the angle of the wrist on the elevator.
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param angle The angle to set the wrist to
 * @param wrist The wrist to act on
 */
public class WristAction(
    timeoutseconds: Double,
    val angle: Double,
    val wrist: Wrist
) : Action(timeoutseconds) {

    public override fun start() {
        wrist.setAngle(angle)
    }

    // Called every tick
    public override fun update() {}

    // Returns true if we are ready to move on to the next action
    public override fun next(): Boolean {
        return (super.next() || wrist.wristAngle == angle)
    }
}
