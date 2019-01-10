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

    // Keep a reference to the drivetrain locally for simplicity's sake
    private val mDrivetrain: Drivetrain = subsystemsManager.drivetrain

    // The actuall class from MonkeyLib that does all the math for path following
    private val mPathfollower: PathFollower = PathFollower(path,
                            Constants.Dimensions.WHEEL_BASE, Constants.LOOKAHEAD_DISTANCE)

    // Called every tick
    public override fun update() {

        // Get the required motor velocity for the current position on the path
        val pathfolloweroutput: PathFollower.PathFollowerOutput = mPathfollower.update(mDrivetrain.pose)

        // Set the drivetrain to the right velocity
        mDrivetrain.setVelocity(pathfolloweroutput.leftVelocity, pathfolloweroutput.rightVelocity)
    }

    // Returns true if we are ready to move on to the next action
    public override fun next(): Boolean {
        return (super.next() || mPathfollower.doneWithPath(mDrivetrain.pose))
    }
}
