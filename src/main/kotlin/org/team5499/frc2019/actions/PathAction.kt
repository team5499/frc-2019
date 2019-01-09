package org.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.path.Path
import org.team5499.monkeyLib.path.PathFollower

public class PathAction(
    timeoutseconds: Double,
    path: Path,
    subsystemsManager: SubsystemsManager
) : Action(timeoutseconds) {

    private val mDrivetrain: Drivetrain = subsystemsManager.drivetrain
    private val mPathfollower: PathFollower = PathFollower(path,
                            Constants.Dimensions.WHEEL_BASE, Constants.LOOKAHEAD_DISTANCE)

    public override fun update() {
    }
}
