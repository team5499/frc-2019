package org.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain

import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.math.geometry.Pose2d

public class TurnAction(
    timeoutseconds: Double,
    degrees: Double,
    subsystemsManager: SubsystemsManager
) : Action(timeoutseconds) {

    // Keep a reference to the drivetrain locally for simplicity's sake
    private val mDrivetrain: Drivetrain = subsystemsManager.drivetrain

    // Keep track of the initial pose so we know when to end the auto command
    private val mInitialPose: Pose2d = mDrivetrain.pose

    // Called when the action starts
    public override fun start() {
        super.start()

        mDrivetrain.setTurn(degrees)
    }
}
