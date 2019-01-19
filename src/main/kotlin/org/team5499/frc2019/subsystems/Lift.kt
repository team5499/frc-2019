package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.util.Utils

import org.team5499.frc2019.Constants

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.InvertType

@SuppressWarnings("MagicNumber", "TooManyFunctions")
public class Lift(masterTalon: LazyTalonSRX, slaveTalon: LazyTalonSRX) : Subsystem() {

    companion object {
        private const val kElevatorSlot = 0
        private const val kTicksPerRotation = 1024 // check this
        private const val kZeroingThreshold = 500

        public const val kMaxElevatorTicks = 8300 // check this
        public const val kMinElevatorTicks = 50 // check this
    }

    public enum class ElevatorMode {
        OPEN_LOOP,
        VELOCITY,
        MOTION_MAGIC,
        ZERO
    }

    private val mMaster: LazyTalonSRX
    private val mSlave: LazyTalonSRX

    private var mElevatorMode: ElevatorMode

    private var mZeroed: Boolean
    private var mSetpoint: Double

    // first stage numbers
    public val firstStagePositionRaw: Int
        get() = mMaster.getSensorCollection().getQuadraturePosition()

    public val firstStagePositionInches: Double
        get() = Utils.encoderTicksToInches(
            kTicksPerRotation,
            Constants.Dimensions.SPROCKET_CIR_LIFT,
            firstStagePositionRaw
        )

    public val firstStagePositionErrorRaw: Int
        get() = mMaster.getClosedLoopError(0)

    public val firstStagePositionErrorInches: Double
        get() = Utils.encoderTicksToInches(
            kTicksPerRotation,
            Constants.Dimensions.SPROCKET_CIR_LIFT,
            firstStagePositionErrorRaw
        )

    public val firstStageVelocityRaw: Int
        get() = mMaster.getSensorCollection().getQuadratureVelocity()

    public val firstStageVelocityInchesPerSecond: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            kTicksPerRotation,
            Constants.Dimensions.SPROCKET_CIR_LIFT,
            firstStageVelocityRaw
        )

    public val firstStageVelocityErrorRaw: Int
        get() = mMaster.getClosedLoopError(0)

    public val firstStageVelocityErrorInchesPerSecond: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            kTicksPerRotation,
            Constants.Dimensions.SPROCKET_CIR_LIFT,
            firstStageVelocityErrorRaw
        )

    // carriage numbers
    public val secondStagePositionRaw: Int
        get() = 2 * firstStagePositionRaw

    public val secondStagePositionInches: Double
        get() = 2.0 * firstStagePositionInches

    public val secondStagePositionErrorRaw: Int
        get() = 2 * firstStagePositionErrorRaw

    public val secondStagePositionErrorInches: Double
        get() = 2.0 * firstStagePositionErrorInches

    public val secondStageVelocityRaw: Int
        get() = 2 * firstStageVelocityRaw

    public val secondStageVelocityInchesPerSecond: Double
        get() = 2.0 * firstStageVelocityInchesPerSecond

    public val secondStageVelocityErrorRaw: Int
        get() = 2 * firstStageVelocityErrorRaw

    public val secondStageVelocityErrorInchesPerSecond: Double
        get() = 2.0 * firstStageVelocityErrorInchesPerSecond

    private var mBrakeMode: Boolean = false
        set(value) {
            if (value == field) return
            if (value)
                mMaster.setNeutralMode(NeutralMode.Brake)
            else
                mMaster.setNeutralMode(NeutralMode.Coast)
            field = value
        }

    init {
        this.mMaster = masterTalon.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10)
            setSensorPhase(true) // check
            setNeutralMode(NeutralMode.Coast)
            setInverted(true) // check this

            configClosedLoopPeakOutput(kElevatorSlot, 1.0)

            config_kP(kElevatorSlot, 2.5, 10)
            config_kI(kElevatorSlot, 0.0, 10)
            config_kD(kElevatorSlot, 0.2, 10)
            config_kF(kElevatorSlot, 0.0, 10)
            configMotionCruiseVelocity(1000, 10)
            configMotionAcceleration(900, 10)
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

        this.mSlave = slaveTalon.apply {
            follow(mMaster)
            setInverted(InvertType.FollowMaster)
        }

        mElevatorMode = ElevatorMode.ZERO
        mZeroed = false
        // mEncoderPresent = false
        mSetpoint = 0.0

        // set brake
        mBrakeMode = false

        // set speed
        mMaster.set(ControlMode.PercentOutput, 0.0)
    }

    private fun setZero() {
        mMaster.getSensorCollection().setQuadraturePosition(0, 0)
    }

    public fun setPower(power: Double) {
        mElevatorMode = ElevatorMode.OPEN_LOOP
        val limitedPower = Utils.limit(power, -0.6, 1.0)
        mBrakeMode = true
        mMaster.set(ControlMode.PercentOutput, limitedPower)
    }

    public fun setPositionRaw(ticks: Int) {
        if (!mZeroed) return
        mBrakeMode = false
        val positionTicks = Utils.limit(
            ticks.toDouble(),
            kMinElevatorTicks.toDouble(),
            kMaxElevatorTicks.toDouble()
        )
        mElevatorMode = ElevatorMode.MOTION_MAGIC
        mSetpoint = positionTicks
    }

    public fun setPosition(positionInches: Double) {
        val positionTicks = Utils.inchesToEncoderTicks(
            kTicksPerRotation,
            Constants.Dimensions.SPROCKET_CIR_LIFT,
            positionInches
        )
        setPositionRaw(positionTicks.toInt())
    }

    public fun setCarriagePosition(positionInches: Double) {
        setPosition(0.5 * positionInches)
    }

    public fun setVelocityRaw(ticksPer100ms: Int) {
        if (!mZeroed) return
        mBrakeMode = false
        val speed = Utils.limit(ticksPer100ms.toDouble(), Constants.PID.MAX_LIFT_VELOCITY_SETPOINT.toDouble())
        mElevatorMode = ElevatorMode.VELOCITY
        mSetpoint = speed
    }

    public fun setVelocity(inchesPerSecond: Double) {
        val speed = Utils.inchesPerSecondToEncoderTicksPer100Ms(
            kTicksPerRotation,
            Constants.Dimensions.SPROCKET_CIR_LIFT,
            inchesPerSecond
        )
        setVelocityRaw(speed.toInt())
    }

    public fun setCarriageVelocity(inchesPerSecond: Double) {
        setVelocity(inchesPerSecond * 0.5)
    }

    public override fun update() {
        // mEncoderPresent = mMaster.getSensorCollection().getPulseWidthRiseToRiseUs() != 0
        if (!mZeroed) mElevatorMode = ElevatorMode.ZERO
        when (mElevatorMode) {
            ElevatorMode.ZERO -> {
                // drive downwards until hall effect detects carriage
                if (mZeroed || mMaster.getSensorCollection().getAnalogIn() > kZeroingThreshold) {
                    mZeroed = true
                    mMaster.set(ControlMode.PercentOutput, 0.0)
                    mSetpoint = 0.0
                    setZero()
                    mElevatorMode = ElevatorMode.OPEN_LOOP
                }
                mMaster.set(ControlMode.PercentOutput, -0.05)
            }
            ElevatorMode.VELOCITY -> {
                mMaster.set(ControlMode.Velocity, mSetpoint)
            }
            ElevatorMode.OPEN_LOOP -> {
                mMaster.set(ControlMode.PercentOutput, mSetpoint)
            }
            ElevatorMode.MOTION_MAGIC -> {
                mMaster.set(ControlMode.MotionMagic, mSetpoint)
            }
        }
    }

    public override fun stop() {
        mBrakeMode = true
        setPower(0.0)
        mMaster.neutralOutput()
    }

    public override fun reset() {
        stop()
    }
}
