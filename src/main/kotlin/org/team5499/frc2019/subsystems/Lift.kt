package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.util.Utils

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice

@SuppressWarnings("MagicNumber")
public class Lift(masterTalon: LazyTalonSRX /*, slaveTalon: LazyTalonSRX*/) : Subsystem() {

    companion object {
        private const val kElevatorSlot = 0
        public const val kMaxElevatorTicks = 8200 // check this
        public const val kMinElevatorTicks = 10 // check this
        private const val kTicksPerInch = 1024 // check this
        private const val kPowerSafetyRange = 100 // ticks
    }

    public enum class ElevatorMode {
        OPEN_LOOP,
        MOTION_MAGIC,
        ZERO
    }

    private val mMaster: LazyTalonSRX
    // private val mSlave: LazyTalonSRX

    private var mElevatorMode: ElevatorMode

    private var mEncoderPresent: Boolean
    public var zeroed: Boolean
        private set

    public val positionTicks: Int
        get() = mMaster.getSelectedSensorPosition(0)

    public val positionInches: Double // inches
        get() = (positionTicks / kTicksPerInch.toDouble()).toDouble()

    init {
        this.mMaster = masterTalon.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10)
            setSensorPhase(false) // check
            setNeutralMode(NeutralMode.Coast)
            setInverted(false) // check this

            config_kP(kElevatorSlot, 0.1, 10)
            config_kI(kElevatorSlot, 0.0, 10)
            config_kD(kElevatorSlot, 0.0, 10)
            config_kF(kElevatorSlot, 0.0, 10)
            configMotionCruiseVelocity(0, 200)
            configMotionAcceleration(0, 100)
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

        /*
        this.mSlave = slaveTalon.apply {
            follow(mMaster)
            setInverted(InvertType.FollowMaster)
        }
        */

        mElevatorMode = ElevatorMode.ZERO
        zeroed = false
        mEncoderPresent = false

        mMaster.set(ControlMode.PercentOutput, 0.0)
    }

    public fun zero() {
        mMaster.getSensorCollection().setQuadraturePosition(0, 0)
    }

    public fun setPower(power: Double) {
        mElevatorMode = ElevatorMode.OPEN_LOOP
        val limitedPower = Utils.limit(power, -0.6, 1.0)
        mMaster.set(ControlMode.PercentOutput, limitedPower)

        // if (!mEncoderPresent) mMaster.set(ControlMode.PercentOutput, limitedPower)

        // if (positionTicks < (kMinElevatorTicks + kPowerSafetyRange) && limitedPower < 0.0) {
        //     // check if close to bottom stop and negative power
        //     setPositionRaw(kMinElevatorTicks)
        // } else if (positionTicks > (kMaxElevatorTicks - kPowerSafetyRange) && limitedPower > 0.0) {
        //     // check if close to top stop and positive power
        //     setPositionRaw(kMaxElevatorTicks)
        // } else {
        //     mMaster.set(ControlMode.PercentOutput, limitedPower)
        // }
    }

    public fun setPositionRaw(ticks: Int) {
        // if (!zeroed) return
        // if (!mEncoderPresent) {
        //     DriverStation.reportWarning("Elevator encoder is not present! Please use manual power", false)
        //     setPower(0.0)
        //     return
        // }
        mElevatorMode = ElevatorMode.MOTION_MAGIC
        val positionTicks = Utils.limit(ticks.toDouble(), kMinElevatorTicks.toDouble(), kMaxElevatorTicks.toDouble())
        mMaster.set(ControlMode.MotionMagic, positionTicks)
    }

    public fun setPosition(positionInches: Double) {
        val positionTicks = positionInches * kTicksPerInch
        setPositionRaw(positionTicks.toInt())
    }

    public override fun update() {
        mEncoderPresent = mMaster.getSensorCollection().getPulseWidthRiseToRiseUs() != 0
        println("elevator position ticks: ${mMaster.getSensorCollection().getQuadraturePosition()}")
    }

    public override fun stop() {
        setPower(0.0)
        mMaster.neutralOutput()
    }

    public override fun reset() {
    }
}
