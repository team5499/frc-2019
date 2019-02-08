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
    }

    public enum class ElevatorMode {
        OPEN_LOOP,
        VELOCITY,
        MOTION_MAGIC,
        ZERO
    }

    public enum class ElevatorHeight(val carriageHeightInches: Double = 0.45) {
        BOTTOM(0.45),
        HATCH_LOW(9.0),
        HATCH_MID(1.0),
        HATCH_HIGH(2.0),
        BALL_LOW(3.0),
        BALL_MID(4.0),
        BALL_HIGH(5.0),
        BALL_HUMAN_PLAYER(21.0)
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
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            firstStagePositionRaw
        )

    public val firstStagePositionErrorRaw: Int
        get() = mMaster.getClosedLoopError(0)

    public val firstStagePositionErrorInches: Double
        get() = Utils.encoderTicksToInches(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            firstStagePositionErrorRaw
        )

    public val firstStageVelocityRaw: Int
        get() = mMaster.getSelectedSensorVelocity(0)

    public val firstStageVelocityInchesPerSecond: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            firstStageVelocityRaw
        )

    public val firstStageVelocityErrorRaw: Int
        get() = mMaster.getClosedLoopError(0)

    public val firstStageVelocityErrorInchesPerSecond: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
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
            setInverted(true) // check this

            configClosedLoopPeakOutput(kElevatorSlot, 1.0)

            config_kP(kElevatorSlot, Constants.Lift.KP, 10)
            config_kI(kElevatorSlot, Constants.Lift.KI, 10)
            config_kD(kElevatorSlot, Constants.Lift.KD, 10)
            config_kF(kElevatorSlot, Constants.Lift.KF, 10)
            configMotionCruiseVelocity(1000, 10)
            configMotionAcceleration(800, 10)
            selectProfileSlot(kElevatorSlot, 0)

            enableCurrentLimit(true)
            configPeakCurrentDuration(0, 10)
            configPeakCurrentLimit(0, 10)
            configContinuousCurrentLimit(25, 10) // amps
            enableVoltageCompensation(false)
            configForwardSoftLimitThreshold(Constants.Lift.MAX_ENCODER_TICKS, 10)
            configReverseSoftLimitThreshold(Constants.Lift.MIN_ENCODER_TICKS, 10)
            configForwardSoftLimitEnable(true, 10)
            configReverseSoftLimitEnable(true, 10)
        }

        this.mSlave = slaveTalon.apply {
            follow(mMaster)
            setInverted(InvertType.FollowMaster)
        }

        mElevatorMode = ElevatorMode.ZERO
        mZeroed = false // CHANGE THIS TO FALSE
        // mEncoderPresent = false
        mSetpoint = 0.0

        // set brake
        mBrakeMode = true

        // set speed
        mMaster.set(ControlMode.PercentOutput, 0.0)
    }

    public fun setZero() {
        mMaster.setSelectedSensorPosition(0, 0, 0)
    }

    public fun setPower(power: Double) {
        mElevatorMode = ElevatorMode.OPEN_LOOP
        val limitedPower = Utils.limit(power, -0.6, 1.0)
        mBrakeMode = true
        mSetpoint = limitedPower
    }

    public fun setPositionRaw(ticks: Int) {
        if (!mZeroed) return
        mBrakeMode = true
        val positionTicks = Utils.limit(
            ticks.toDouble(),
            Constants.Lift.MIN_ENCODER_TICKS.toDouble(),
            Constants.Lift.MAX_ENCODER_TICKS.toDouble()
        )
        mElevatorMode = ElevatorMode.MOTION_MAGIC
        mSetpoint = positionTicks
        mMaster.set(ControlMode.MotionMagic, mSetpoint)
    }

    public fun setPosition(positionInches: Double) {
        val positionTicks = Utils.inchesToEncoderTicks(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            positionInches
        )
        setPositionRaw(positionTicks.toInt())
    }

    public fun setCarriagePosition(positionInches: Double) {
        setPosition(0.5 * positionInches)
    }

    public fun setIntakeHeight(height: ElevatorHeight) {
        setCarriagePosition(height.carriageHeightInches)
    }

    public fun setVelocityRaw(ticksPer100ms: Int) {
        if (!mZeroed) return
        mBrakeMode = false
        val speed = Utils.limit(ticksPer100ms.toDouble(), Constants.Lift.MAX_VELOCITY_SETPOINT.toDouble())
        mElevatorMode = ElevatorMode.VELOCITY
        mSetpoint = speed
        mMaster.set(ControlMode.Velocity, speed)
    }

    public fun setVelocity(inchesPerSecond: Double) {
        val speed = Utils.inchesPerSecondToEncoderTicksPer100Ms(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            inchesPerSecond
        )
        setVelocityRaw(speed.toInt())
    }

    public fun setCarriageVelocity(inchesPerSecond: Double) {
        setVelocity(inchesPerSecond * 0.5)
    }

    public override fun update() {
        // mEncoderPresent = mMaster.getSensorCollection().getPulseWidthRiseToRiseUs() != 0
        // println("elevator speed: ${mMaster.getSelectedSensorVelocity(0)}")
        // println("elevator position: $firstStagePositionRaw")
        // println("elevator setpoint: ${mMaster.getClosedLoopTarget(0)}")
        if (!mZeroed) {
            mElevatorMode = ElevatorMode.ZERO
            mMaster.configReverseSoftLimitEnable(false)
        }
        when (mElevatorMode) {
            ElevatorMode.ZERO -> {
                // drive downwards until hall effect detects carriage
                if (Math.abs(mMaster.getSelectedSensorVelocity(0)) < Constants.Lift.ZEROING_THRESHOLD) {
                    mZeroed = true
                    mMaster.set(ControlMode.PercentOutput, 0.0)
                    mSetpoint = 0.0
                    setZero()
                    mElevatorMode = ElevatorMode.OPEN_LOOP
                    mMaster.configReverseSoftLimitEnable(true)
                    println("Elevator zeroed!")
                }
                // println("going down!")
                mMaster.set(ControlMode.PercentOutput, -0.3)
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
        mSetpoint = 0.0
        mMaster.neutralOutput()
    }

    public override fun reset() {
        stop()
    }
}
