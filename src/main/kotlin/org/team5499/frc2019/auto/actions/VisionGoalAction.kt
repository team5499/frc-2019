package org.team5499.frc2019.auto.actions

import org.team5499.monkeyLib.auto.Action

import org.team5499.frc2019.subsystems.Vision
import org.team5499.frc2019.subsystems.Drivetrain

public class VisionGoalAction(timeout: Double, val vision: Vision, val drivetrain: Drivetrain) : Action(timeout) {

    public override fun start() {
        vision.ledState = Vision.LEDState.ON
    }

    public override fun update() {
        if (!vision.hasValidTarget) {
            drivetrain.setPercent(0.0, 0.0)
        } else {
            var steer = (vision.targetXOffset) * 0.011
            var drive = (15.0 - vision.targetArea) * 0.1

            if (drive > 0.5) drive = 0.2
            drivetrain.setPercent(drive + steer, drive - steer)
        }
    }

    public override fun next(): Boolean {

        return vision.targetArea > 13.25
    }

    public override fun finish() {
        vision.ledState = Vision.LEDState.OFF
    }
}
