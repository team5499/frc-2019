package org.team5499.frc2019.auto.actions

import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.math.pid.PIDF

import org.team5499.frc2019.subsystems.Vision
import org.team5499.frc2019.subsystems.Drivetrain
import org.team5499.frc2019.Constants

public class VisionGoalAction(
    timeout: Double,
    val goal: VisionGoal,
    val vision: Vision,
    val drivetrain: Drivetrain
) : Action(timeout) {

    public enum class VisionGoal { HATCH_TARGET, BALL_TARGET }

    private val mAnglePID: PIDF
    private val mDistancePID: PIDF

    init {
        mAnglePID = PIDF(
            Constants.Vision.ANGLE_KP,
            Constants.Vision.ANGLE_KI,
            Constants.Vision.ANGLE_KI,
            Constants.Vision.ANGLE_KD,
            false
        )
        mDistancePID = PIDF(
            Constants.Vision.DISTANCE_KP,
            Constants.Vision.DISTANCE_KI,
            Constants.Vision.DISTANCE_KD,
            Constants.Vision.DISTANCE_KF,
            false
        )
    }

    public override fun start() {

        // reset pid for dashboard
        mAnglePID.reset()
        mDistancePID.reset()

        mAnglePID.kP = Constants.Vision.ANGLE_KP
        mAnglePID.kI = Constants.Vision.ANGLE_KI
        mAnglePID.kD = Constants.Vision.ANGLE_KD
        mAnglePID.kF = Constants.Vision.ANGLE_KF

        mDistancePID.kP = Constants.Vision.DISTANCE_KP
        mDistancePID.kI = Constants.Vision.DISTANCE_KI
        mDistancePID.kD = Constants.Vision.DISTANCE_KD
        mDistancePID.kF = Constants.Vision.DISTANCE_KF

        mAnglePID.setpoint = -Constants.Vision.CAMERA_HORIZONTAL_ANGLE
        mDistancePID.setpoint = Constants.Vision.TARGET_DISTANCE

        // turn on leds
        // vision.ledState = Vision.LEDState.ON
    }

    public override fun update() {

        if (!vision.hasValidTarget) {
            drivetrain.setVelocity(0.0, 0.0)
        } else {
            mAnglePID.processVariable = -vision.targetXOffset
            var steer = mAnglePID.calculate()

            // mDistancePID.processVariable = when (goal) {
            //     VisionGoal.BALL_TARGET -> vision.distanceToBallTarget
            //     VisionGoal.HATCH_TARGET -> vision.distanceToHatchTarget
            // }
            mDistancePID.processVariable = -vision.distanceToTarget
            val drive = mDistancePID.calculate()

            val left = drive + steer
            val right = drive - steer

            drivetrain.setVelocity(left, right)
        }
    }

    public override fun next(): Boolean {
        return super.timedOut() || (
            vision.distanceToTarget < Constants.Vision.TARGET_DISTANCE // &&
            // abs(mAnglePID.error) < Constants.Vision.ACCEPTABLE_ANGLE_ERROR &&
            // abs(mDistancePID.error) < Constants.Vision.ACCEPTABLE_DISTANCE_ERROR
        )
    }

    public override fun finish() {
        drivetrain.setVelocity(0.0, 0.0)
    }
}
