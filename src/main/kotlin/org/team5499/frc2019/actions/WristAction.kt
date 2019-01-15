package org.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Wrist

import org.team5499.monkeyLib.auto.Action

/**
 * An action that sets the angle of the wrist on the elevator.
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param wristAngle The angle the wrist will go to
 * @param subsystemsManager The subsystems manager to get the drivetrain from
 */
public class WristAction(
    timeoutseconds: Double,
    angle: Double,
    subsystemsManager: SubsystemsManager
) : Action(timeoutseconds) {

    // Keep a reference to the drivetrain locally for simplicity's sake
    private val mWrist: Wrist = subsystemsManager.wrist

    private val wristAngle = angle

    public override fun start() {
        mWrist.setAngle(wristAngle)
    }

    // Called every tick
    public override fun update() {}

    // Returns true if we are ready to move on to the next action
    public override fun next(): Boolean {
        return (super.next() || mWrist.wristAngle == wristAngle)
    }
}
