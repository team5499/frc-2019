package org.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.Drivetrain

import org.team5499.monkeyLib.auto.Action

/**
 * An action that finishes when the robot crosses an x boundry line
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param xLine The x boundary line
 * @param lessThan Boolean to determine if the robot is behind the boundary line
 * @param drivetrain The drivetrain to act on
 */
public class CrossedXBoundaryAction(
    xLine: Double,
    lessThan: Boolean = false,
    val drivetrain: Drivetrain
) : Action(0.0) {

    private val mLineCoord: Double
    private val mLessThan: Boolean

    init {
        mLineCoord = xLine
        mLessThan = lessThan
    }

    public override fun next(): Boolean {
        if (mLessThan) {
            return (drivetrain.pose.translation.x < mLineCoord)
        } else {
            return (drivetrain.pose.translation.x > mLineCoord)
        }
    }
}
