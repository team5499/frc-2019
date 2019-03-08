package org.team5499.frc2019.auto.actions

import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.path.Path
import org.team5499.monkeyLib.path.PathFollower

import org.team5499.dashboard.Dashboard

/**
 * An action that will follow the given path.
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param path The path to follow
 * @param drivetrain The drivetrain to act on
 */
public class PathAction(
    timeoutSeconds: Double,
    path: Path,
    val drivetrain: Drivetrain
) : Action(timeoutSeconds) {

    // The actuall class from MonkeyLib that does all the math for path following
    private val mPathfollower: PathFollower = PathFollower(path,
                            Constants.Drivetrain.WHEEL_BASE, Constants.Auto.LOOKAHEAD_DISTANCE)

    init {
        Dashboard.addInlineListener("Constants.Auto.LOOKAHEAD_DISTANCE") {
            _: String, value: Double? ->
            if (value != null) {
                mPathfollower.lookaheadDistance = value
            }
        }
    }

    // Called every tick
    public override fun update() {

        // Get the required motor velocity for the current position on the path
        val pathfolloweroutput: PathFollower.PathFollowerOutput = mPathfollower.update(drivetrain.pose)

        // Set the drivetrain to the right velocity
        drivetrain.setVelocity(pathfolloweroutput.leftVelocity, pathfolloweroutput.rightVelocity)
    }

    // Returns true if we are ready to move on to the next action
    public override fun next(): Boolean {
        return (super.next() || mPathfollower.doneWithPath(drivetrain.pose))
    }
}
