package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.util.Utils

import com.ctre.phoenix.motorcontrol.InvertType
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice

import org.team5499.frc2019.Constants

@SuppressWarnings("MagicNumber")
public class Lift : Subsystem() {

    companion object {
        private const val kElevatorSlot = 0
        private const val kMaxElevatorTicks = 1000 // check this
        private const val kMinElevatorTicks = 0 // check this
        private const val kTicksPerInch = 1024 // check this
    }

    public enum class ElevatorMode {
        OPEN_LOOP,
        MOTION_MAGIC,
        ZERO
    }

    private val mMaster: LazyTalonSRX
    private val mSlave: LazyTalonSRX

    private var mElevatorMode: ElevatorMode

    public var zeroed: Boolean

    public val positionInches: Double // inches
        get() = (mMaster.getSelectedSensorPosition(0) / kTicksPerInch.toDouble()).toDouble()

    public val velocity: Double // inches / second
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.SPROCKET_CIR_LIFT,
            mMaster.sensorCollection.quadratureVelocity
        )

    public val positionError: Double
        get() = Utils.encoderTicksToInches(
            Constants.Units.ENCODER_TICKS_PER_ROTATION,
            Constants.Dimensions.SPROCKET_CIR_LIFT,
            mMaster.getClosedLoopError(0)
        )

    init {
        mMaster = LazyTalonSRX(Constants.HardwarePorts.LIFT_MASTER).apply {
            configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10)
            setSensorPhase(false) // check
            setNeutralMode(NeutralMode.Coast)
            setInverted(false) // check this

            config_kP(kElevatorSlot, 0.0, 10)
            config_kI(kElevatorSlot, 0.0, 10)
            config_kD(kElevatorSlot, 0.0, 10)
            config_kF(kElevatorSlot, 0.0, 10)
            configMotionCruiseVelocity(0, 10)
            configMotionAcceleration(0, 10)
            selectProfileSlot(kElevatorSlot, 0)

            enableCurrentLimit(true)
            configPeakCurrentDuration(0, 10)
            configPeakCurrentLimit(0, 10)
            configContinuousCurrentLimit(25, 10) // amps
            enableVoltageCompensation(false)
            configForwardSoftLimitThreshold(kMaxElevatorTicks, 10)
            configReverseSoftLimitThreshold(kMinElevatorTicks, 10)
            configForwardSoftLimitEnable(true, 10)
            configReverseSoftLimitEnable(true, 10)
        }

        mSlave = LazyTalonSRX(Constants.HardwarePorts.LIFT_SLAVE).apply {
            follow(mMaster)
            setInverted(InvertType.FollowMaster)
        }

        mElevatorMode = ElevatorMode.ZERO
        zeroed = false

        mMaster.set(ControlMode.PercentOutput, 0.0)
    }

    public fun setPower(power: Double) {
        mElevatorMode = ElevatorMode.OPEN_LOOP
        val limitedPower = Utils.limit(power, -0.6, 1.0)
        mMaster.set(ControlMode.PercentOutput, power)
    }

    public fun setPosition(positionInches: Double) {
        mElevatorMode = ElevatorMode.MOTION_MAGIC
        var positionTicks = positionInches * kTicksPerInch
        positionTicks = Utils.limit(positionTicks, kMinElevatorTicks.toDouble(), kMaxElevatorTicks.toDouble())
        mMaster.set(ControlMode.MotionMagic, positionTicks)
    }

    public override fun update() {
    }

    public override fun stop() {
        setPower(0.0)
        mMaster.neutralOutput()
    }

    public override fun reset() {
    }
}
