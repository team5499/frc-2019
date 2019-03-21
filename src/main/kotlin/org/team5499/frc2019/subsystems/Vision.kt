package org.team5499.frc2019.subsystems

import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.Subsystem

import edu.wpi.first.networktables.NetworkTableInstance

import kotlin.math.tan

public class Vision : Subsystem() {

    @Suppress("MagicNumber")
    public enum class LEDState(val value: Int) { PIPELINE(0), OFF(1), BLINK(2), ON(3) }

    @Suppress("MagicNumber")
    public enum class VisionMode(val value: Int) { DRIVER(9), VISION(0) } // pipeline

    private val mTable = NetworkTableInstance.getDefault.getTable("limelight")

    public var ledState: LEDState = LEDState.OFF
        set(value) {
            table.getEntry("ledMode").setNumber(value.value)
            field = value
        }
    public var visionMode: VisionMode = VisionMode.VISION
        set(value) {
            table.getEntry("pipeline").setNumber(value.value)
            field = value
        }
    public val hasValidTarget: Boolean
        get() {
            return if (
                table.getEntry("tv").getDouble(0.0) == 1.0
            ) true
            else false
        }
    public val targetXOffset: Double
        get() {
            return table.getEntry("tx").getDouble(0.0)
        }
    public val targetYOffset: Double
        get() {
            return table.getEntry("ty").getDouble(0.0)
        }
    public val targetSkew: Double
        get() {
            return table.getEntry("ts").getDouble(0.0)
        }
    public val targetArea: Double
        get() {
            return table.getEntry("ta").getDouble(0.0)
        }

    // http://docs.limelightvision.io/en/latest/cs_estimating_distance.html
    public val distanceToHatchTarget: Double
        get() {
            // return (Constants.Vision.HATCH_TARGET_HEIGHT - Constants.Vision.CAMERA_HEIGHT) /
            //     tan(Constants.Vision.CAMERA_VERTICAL_ANGLE + targetYOffset)
            val table = NetworkTableInstance.getDefault().getTable("limelight")
                .getEntry("camtran").getDoubleArray(arrayOf<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
            val x = table[0]
            val z = table[2]
            println("vision coords: $x, $z")
            return Math.hypot(x, z)
        }
    public val distanceToBallTarget: Double
        get() {
            return (Constants.Vision.BALL_TARGET_HEIGHT - Constants.Vision.CAMERA_HEIGHT) /
                tan(Constants.Vision.CAMERA_VERTICAL_ANGLE + targetYOffset)
        }

    init {
        table.getEntry("camMode").setNumber(0)
        table.getEntry("stream").setNumber(0)
        table.getEntry("ledMode").setNumber(0)
    }

    public override fun update() {}

    public override fun stop() {}

    public override fun reset() {}
}
