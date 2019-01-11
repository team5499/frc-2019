package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.util.Utils
import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX

import org.team5499.frc2019.Constants

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode

@SuppressWarnings("MagicNumber")

public class Wrist : Subsystem() {

    private val mTalon: LazyTalonSRX

    companion object {
        private const val kWristSlot = 0
        private const val kMaxWristTicks = Constants.Units.ENCODER_TICKS_PER_ROTATION / 4
        private const val kMinWristTicks = 0
        private const val kTicksPerDegree = Constants.Units.ENCODER_TICKS_PER_ROTATION / 360
    }

    init {
        mTalon = LazyTalonSRX(Constants.HardwarePorts.WRIST_MASTER).apply {
            configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10)

            setSensorPhase(false) // check
            setNeutralMode(NeutralMode.Coast)
            setInverted(false) // check this

            config_kP(kWristSlot, 0.0, 10)
            config_kI(kWristSlot, 0.0, 10)
            config_kD(kWristSlot, 0.0, 10)
            config_kF(kWristSlot, 0.0, 10)
            configMotionCruiseVelocity(0, 10) // check these
            configMotionAcceleration(0, 10)
            selectProfileSlot(kWristSlot, 0)

            enableCurrentLimit(true)
            configPeakCurrentDuration(0, 10)
            configPeakCurrentLimit(0, 10)
            configContinuousCurrentLimit(25, 10)
            enableVoltageCompensation(false)
            configForwardSoftLimitThreshold(kMaxWristTicks, 10)
            configReverseSoftLimitThreshold(kMinWristTicks, 10)
            configForwardSoftLimitEnable(true, 10)
            configReverseSoftLimitEnable(true, 10)
        }
    }

    fun setPower(power: Double) {
        val limitedPower = Utils.limit(power, 1.0)
        var currentDegrees = mTalon.getSelectedSensorPosition() / kTicksPerDegree
        if (currentDegrees > 90.0) {
            setAngle(90.0)
        } else if (currentDegrees < 0.0) {
            setAngle(0.0)
        } else {
            mTalon.set(ControlMode.PercentOutput, limitedPower)
        }
    }

    fun setAngle(angleDegrees: Double) {
        var positionTicks = angleDegrees * kTicksPerDegree
        positionTicks = Utils.limit(positionTicks, kMinWristTicks.toDouble(), kMaxWristTicks.toDouble())
        mTalon.set(ControlMode.MotionMagic, positionTicks)
    }

    public override fun update() {
    }

    public override fun stop() {
    }

    public override fun reset() {
    }
}
