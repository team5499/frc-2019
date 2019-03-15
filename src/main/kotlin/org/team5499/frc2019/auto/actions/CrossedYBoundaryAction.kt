package org.team5499.frc2019.auto.actions

import org.team5499.frc2019.subsystems.Drivetrain

import org.team5499.monkeyLib.auto.Action
import org.tinylog.Logger

/**
 * An action that finishes when the robot crosses a y boundry line
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param yLine The y boundary line
 * @param lessThan Boolean to determine if the robot is behind the boundary line
 * @param drivetrain The drivetrain to act on
 */
public class CrossedYBoundaryAction(
    yLine: Double,
    lessThan: Boolean = false,
    val drivetrain: Drivetrain
) : Action(0.0) {

    private val mLineCoord: Double
    private val mLessThan: Boolean

    init {
        mLineCoord = yLine
        mLessThan = lessThan
    }

    public override fun start() {
        Logger.tag("ACTIONS").info("Starting y boundary action!" as Any)
    }

    public override fun next(): Boolean {
        if (mLessThan) {
            return (drivetrain.pose.translation.y < mLineCoord)
        } else {
            return (drivetrain.pose.translation.y > mLineCoord)
        }
    }
}
