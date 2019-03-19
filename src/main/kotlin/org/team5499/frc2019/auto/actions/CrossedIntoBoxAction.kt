package org.team5499.frc2019.auto.actions

import org.team5499.frc2019.subsystems.Drivetrain

import org.tinylog.Logger
import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.math.geometry.Vector2

/**
 * An action that finishes when the robot crosses enters a given box
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param lowerLeftCorner The lower left corner of the box
 * @param upperRightCorner The upper right corner of the box
 * @param drivetrain The drivetrain to act on
 */
class CrossedIntoBoxAction(
    val lowerLeftCorner: Vector2,
    val upperRightCorner: Vector2,
    val drivetrain: Drivetrain

) : Action(0.0) {

    public override fun start() {
        Logger.tag("ACTIONS").info("Starting box boundary action!" as Any)
    }

    override fun next(): Boolean {
        val driveTranslation = drivetrain.pose.translation
        return (driveTranslation.x > lowerLeftCorner.x && driveTranslation.x < upperRightCorner.x &&
            driveTranslation.y > lowerLeftCorner.y && driveTranslation.y < upperRightCorner.y)
    }
}
