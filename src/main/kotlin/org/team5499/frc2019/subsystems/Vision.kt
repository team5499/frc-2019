package org.team5499.frc2019.subsystems

import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.Subsystem

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.networktables.NetworkTable

import kotlin.math.tan
import kotlin.math.hypot

public class Vision : Subsystem() {

    @Suppress("MagicNumber")
    public enum class LEDState(val value: Int) { PIPELINE(0), OFF(1), BLINK(2), ON(3) }

    @Suppress("MagicNumber")
    public enum class VisionMode(val value: Int) { DRIVER(9), VISION(0) } // pipeline

    private val mTable: NetworkTable

    private var mBlinking: Boolean
    private var mBlinkDuration: Double

    public var ledState: LEDState = LEDState.OFF
        set(value) {
            mTable.getEntry("ledMode").setNumber(value.value)
            field = value
        }
    public var visionMode: VisionMode = VisionMode.VISION
        set(value) {
            mTable.getEntry("pipeline").setNumber(value.value)
            field = value
        }
    public val hasValidTarget: Boolean
        get() {
            return if (
                mTable.getEntry("tv").getDouble(0.0) == 1.0
            ) true
            else false
        }
    public val targetXOffset: Double
        get() {
            return mTable.getEntry("tx").getDouble(0.0)
        }
    public val targetYOffset: Double
        get() {
            return mTable.getEntry("ty").getDouble(0.0)
        }
    public val targetSkew: Double
        get() {
            return mTable.getEntry("ts").getDouble(0.0)
        }
    public val targetArea: Double
        get() {
            return mTable.getEntry("ta").getDouble(0.0)
        }

    // use this distance method
    public val distanceToTarget: Double
        get() {
            val array = mTable.getEntry("camtran").getDoubleArray(arrayOf<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
            val x = array[0]
            val z = array[2]
            val distance = hypot(x, z)
            return distance
        }

    // http://docs.limelightvision.io/en/latest/cs_estimating_distance.html
    public val distanceToHatchTarget: Double
        get() {
            return (Constants.Vision.HATCH_TARGET_HEIGHT - Constants.Vision.CAMERA_HEIGHT) /
                tan(Constants.Vision.CAMERA_VERTICAL_ANGLE + targetYOffset)
        }
    public val distanceToBallTarget: Double
        get() {
            return (Constants.Vision.BALL_TARGET_HEIGHT - Constants.Vision.CAMERA_HEIGHT) /
                tan(Constants.Vision.CAMERA_VERTICAL_ANGLE + targetYOffset)
        }

    init {
        mTable = NetworkTableInstance.getDefault().getTable("limelight")
        mBlinking = false
        mBlinkDuration = 0.0
        initialize()
    }

    // need to wait for limelight to boot
    public fun initialize() {
        mTable.getEntry("camMode").setNumber(0)
        mTable.getEntry("stream").setNumber(0)
        // mTable.getEntry("ledMode").setNumber(0)
        ledState = LEDState.OFF
        visionMode = VisionMode.VISION
    }

    public fun flashForSeconds(seconds: Double) {
        mBlinking = true
        mBlinkDuration = seconds
        timer.stop()
        timer.reset()
        timer.start()
    }

    public override fun update() {
        if (timer.get() >= mBlinkDuration) {
            mBlinking = false
            mBlinkDuration = 0.0
        } else if (mBlinking) {
            ledState = LEDState.BLINK
        }
    }

    public override fun stop() {
        mBlinking = false
        mBlinkDuration = 0.0
    }

    public override fun reset() {
        stop()
    }
}
