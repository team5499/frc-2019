package frc.team5499.frc2019.actions

import org.team5499.frc2019.subsystems.Drivetrain

import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.math.geometry.Vector2

class CrossedIntoBoxAction(
    timeoutseconds: Double,
    val lowerLeftCorner: Vector2,
    val upperRightCorner: Vector2,
    val drivetrain: Drivetrain

) : Action(timeoutseconds) {

    override fun next(): Boolean {
        val driveTranslation = drivetrain.pose.translation
        return (driveTranslation.x > lowerLeftCorner.x && driveTranslation.x < upperRightCorner.x &&
            driveTranslation.y > lowerLeftCorner.y && driveTranslation.y < upperRightCorner.y)
    }
}
