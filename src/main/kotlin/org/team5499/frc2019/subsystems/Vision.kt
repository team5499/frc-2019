package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem

import edu.wpi.first.networktables.NetworkTableInstance

public class Vision : Subsystem() {

    public enum class LEDState(val value: Int) { PIPELINE(0), OFF(1), BLINK(2), ON(3) }

    public enum class VisionMode(val value: Int) { DRIVER(9), VISION(0) } // pipeline

    public var ledState: LEDState = LEDState.OFF
        set(value) {
            if (value == field) return
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(value.value)
            field = value
        }

    public var visionMode: VisionMode = VisionMode.VISION
        set(value) {
            if (value == field) return
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(value.value)
            field = value
        }

    public var hasValidTarget: Boolean
        private set
    public var targetXOffset: Double
        private set
    public var targetYOffset: Double
        private set
    public var targetSkew: Double
        private set
    public var targetArea: Double
        private set

    init {
        hasValidTarget = false
        targetXOffset = 0.0
        targetYOffset = 0.0
        targetSkew = 0.0
        targetArea = 0.0

        val instance = NetworkTableInstance.getDefault().getTable("limelight")
        instance.getEntry("camMode").setNumber(0)
        instance.getEntry("stream").setNumber(0)
        instance.getEntry("ledMode").setNumber(0)
    }

    public override fun update() {
        val instance = NetworkTableInstance.getDefault().getTable("limelight")

        // get parameters
        targetXOffset = instance.getEntry("tx").getDouble(0.0)
        targetYOffset = instance.getEntry("ty").getDouble(0.0)
        targetSkew = instance.getEntry("ts").getDouble(0.0)
        targetArea = instance.getEntry("ta").getDouble(0.0)
        hasValidTarget = if (instance.getEntry("tv").getDouble(0.0) == 1.0) true else false

    }

    public override fun stop() {}

    public override fun reset() {
        ledState = LEDState.OFF
        visionMode = VisionMode.VISION
        hasValidTarget = false
        targetXOffset = 0.0
        targetYOffset = 0.0
        targetSkew = 0.0
        targetArea = 0.0
    }
}
