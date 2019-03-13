package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem

import edu.wpi.first.networktables.NetworkTableInstance

import kotlin.math.tan

public class Vision : Subsystem() {

    companion object {
        private const val kCameraHeight = 24.0 // inches
        private const val kCameraVerticalAngle = 0.0 // degrees
        private const val kHatchTargetHeight = 20.0 // inches
        private const val kBallTargetHeight = 28.0 // inches
    }

    public enum class LEDState(val value: Int) { PIPELINE(0), OFF(1), BLINK(2), ON(3) }

    public enum class VisionMode(val value: Int) { DRIVER(9), VISION(0) } // pipeline

    public var ledState: LEDState = LEDState.OFF
        set(value) {
            NetworkTableInstance.getDefault().getTable("limelight") .getEntry("ledMode").setNumber(value.value)
            field = value
        }
    public var visionMode: VisionMode = VisionMode.VISION
        set(value) {
            NetworkTableInstance.getDefault().getTable("limelight") .getEntry("pipeline").setNumber(value.value)
            field = value
        }
    public val hasValidTarget: Boolean
        get() {
            return if (NetworkTableInstance.getDefault().getTable("limelight") .getEntry("tv").getDouble(0.0) == 1.0) true
                else false
        }
    public val targetXOffset: Double
        get() {
            return NetworkTableInstance.getDefault().getTable("limelight") .getEntry("tx").getDouble(0.0)
        }
    public val targetYOffset: Double
        get() {
            return NetworkTableInstance.getDefault().getTable("limelight") .getEntry("ty").getDouble(0.0)
        }
    public val targetSkew: Double
        get() {
            return NetworkTableInstance.getDefault().getTable("limelight") .getEntry("ts").getDouble(0.0)
        }
    public val targetArea: Double
        get() {
            return NetworkTableInstance.getDefault().getTable("limelight") .getEntry("ta").getDouble(0.0)
        }
    public val distanceToHatchTarget: Double
        get() {
            return (kHatchTargetHeight - kCameraHeight) / tan(kCameraVerticalAngle + targetYOffset)
        }
    public val distanceToBallTarget: Double
        get() {
            return (kBallTargetHeight - kCameraHeight) / tan(kCameraVerticalAngle + targetYOffset)
        }

    init {
        NetworkTableInstance.getDefault().getTable("limelight") .getEntry("camMode").setNumber(0)
        NetworkTableInstance.getDefault().getTable("limelight") .getEntry("stream").setNumber(0)
        NetworkTableInstance.getDefault().getTable("limelight") .getEntry("ledMode").setNumber(0)
    }

    public override fun update() {}

    public override fun stop() {}

    public override fun reset() {
    }
}
