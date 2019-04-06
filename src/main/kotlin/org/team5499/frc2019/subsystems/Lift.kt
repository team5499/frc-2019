package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.util.Utils

import org.team5499.frc2019.Constants

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.InvertType

import org.team5499.dashboard.Dashboard

@SuppressWarnings("MagicNumber", "TooManyFunctions")
public class Lift(masterTalon: LazyTalonSRX, slaveTalon: LazyTalonSRX) : Subsystem() {

    companion object {
        private const val kElevatorSlot = 0
    }

    private enum class ElevatorMode {
        OPEN_LOOP,
        VELOCITY,
        MOTION_MAGIC,
        ZERO
    }

    public enum class LiftHeight(val carriageHeightInches: () -> Double = { 0.45 }) {
        BOTTOM({ Constants.Lift.STOW_HEIGHT }),
        HATCH_LOW({ Constants.Lift.HATCH_LOW_HEIGHT }),
        HATCH_MID({ Constants.Lift.HATCH_MID_HEIGHT }),
        HATCH_HIGH({ Constants.Lift.HATCH_HIGH_HEIGHT }),
        BALL_LOW({ Constants.Lift.BALL_LOW_HEIGHT }),
        BALL_MID({ Constants.Lift.BALL_MID_HEIGHT }),
        BALL_HIGH({ Constants.Lift.BALL_HIGH_HEIGHT }),
        BALL_HUMAN_PLAYER({ Constants.Lift.BALL_HUMAN_PLAYER_HEIGHT })
    }

    private val mMaster: LazyTalonSRX
    private val mSlave: LazyTalonSRX

    private var mElevatorMode: ElevatorMode
    private var mFirstLoop: Boolean
    public var zeroed: Boolean = false
        set(value) {
            when (value) {
                true -> {
                    println("Elevator Zeroed!")
                }
                false -> {
                    mFirstLoop = true
                }
            }
            field = value
        }

    public var setpoint: Double
    private var mSoftLimitsEnabled: Boolean = false
        set(value) {
            if (value == field) return
            if (value) {
                mMaster.configForwardSoftLimitEnable(true, 0)
                mMaster.configReverseSoftLimitEnable(true, 0)
            } else {
                mMaster.configForwardSoftLimitEnable(false, 0)
                mMaster.configReverseSoftLimitEnable(false, 0)
            }
        }

    // first stage numbers
    public val firstStagePositionRaw: Int
        get() = mMaster.getSelectedSensorPosition(0)

    public val firstStagePositionInches: Double
        get() = Utils.encoderTicksToInches(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            firstStagePositionRaw,
            Constants.Lift.ENCODER_REDUCTION
        )

    public val firstStagePositionErrorRaw: Int
        get() = mMaster.getClosedLoopError(0)

    public val firstStagePositionErrorInches: Double
        get() = Utils.encoderTicksToInches(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            firstStagePositionErrorRaw,
            Constants.Lift.ENCODER_REDUCTION
        )

    public val firstStageVelocityRaw: Int
        get() = mMaster.getSelectedSensorVelocity(0)

    public val firstStageVelocityInchesPerSecond: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            firstStageVelocityRaw,
            Constants.Lift.ENCODER_REDUCTION
        )

    public val firstStageVelocityErrorRaw: Int
        get() = mMaster.getClosedLoopError(0)

    public val firstStageVelocityErrorInchesPerSecond: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            firstStageVelocityErrorRaw,
            Constants.Lift.ENCODER_REDUCTION
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
            if (value) {
                mMaster.setNeutralMode(NeutralMode.Brake)
                mSlave.setNeutralMode(NeutralMode.Brake)
            } else {
                mMaster.setNeutralMode(NeutralMode.Coast)
                mSlave.setNeutralMode(NeutralMode.Coast)
            }
            field = value
        }

    init {
        this.mMaster = masterTalon.apply {
            configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0)
            setSensorPhase(true) // check
            setInverted(false) // check this

            configClosedLoopPeakOutput(kElevatorSlot, 1.0)

            config_kP(kElevatorSlot, Constants.Lift.KP, 0)
            config_kI(kElevatorSlot, Constants.Lift.KI, 0)
            config_kD(kElevatorSlot, Constants.Lift.KD, 0)
            config_kF(kElevatorSlot, Constants.Lift.KF, 0)
            configMotionCruiseVelocity(Constants.Lift.MOTION_MAGIC_VELOCITY, 0)
            configMotionAcceleration(Constants.Lift.MOTION_MAGIC_ACCELERATION, 0)
            selectProfileSlot(kElevatorSlot, 0)
            configAllowableClosedloopError(0, 0, 0)

            enableCurrentLimit(false)
            configPeakCurrentDuration(0, 0)
            configPeakCurrentLimit(0, 0)
            configContinuousCurrentLimit(25, 0) // amps
            enableVoltageCompensation(false)
            configForwardSoftLimitThreshold(Constants.Lift.MAX_ENCODER_TICKS, 0)
            configReverseSoftLimitThreshold(Constants.Lift.MIN_ENCODER_TICKS, 0)
            configForwardSoftLimitEnable(true, 0)
            configReverseSoftLimitEnable(true, 0)
        }

        this.mSlave = slaveTalon.apply {
            follow(mMaster)
            setInverted(InvertType.FollowMaster)
        }

        mElevatorMode = ElevatorMode.ZERO
        zeroed = false // CHANGE THIS TO FALSE
        mSoftLimitsEnabled = false
        // mEncoderPresent = false
        setpoint = 0.0
        mFirstLoop = true

        // set brake
        mBrakeMode = true

        // set speed
        mMaster.set(ControlMode.PercentOutput, 0.0)

        setCallbacks()
    }

    private fun setCallbacks() {
        Dashboard.addInlineListener("Constants.Lift.KP") {
            _: String, value: Double? ->
            if (value != null) {
                mMaster.config_kP(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Lift.KI") {
            _: String, value: Double? ->
            if (value != null) {
                mMaster.config_kI(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Lift.KD") {
            _: String, value: Double? ->
            if (value != null) {
                mMaster.config_kD(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Lift.KF") {
            _: String, value: Double? ->
            if (value != null) {
                mMaster.config_kF(0, value, 0)
            }
        }

        Dashboard.addInlineListener("Constants.Lift.MOTION_MAGIC_VELOCITY") {
            _: String, value: Int? ->
            if (value != null) {
                mMaster.configMotionCruiseVelocity(value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Lift.MOTION_MAGIC_ACCELERATION") {
            _: String, value: Int? ->
            if (value != null) {
                mMaster.configMotionAcceleration(value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Lift.MAX_ENCODER_TICKS") {
            _: String, value: Int? ->
            if (value != null) {
                mMaster.configForwardSoftLimitThreshold(value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Lift.MIN_ENCODER_TICKS") {
            _: String, value: Int? ->
            if (value != null) {
                mMaster.configReverseSoftLimitThreshold(value, 0)
            }
        }
    }

    private fun setZero() {
        mMaster.setSelectedSensorPosition(0, 0, 0)
    }

    public fun setPower(power: Double) {
        mElevatorMode = ElevatorMode.OPEN_LOOP
        val limitedPower = Utils.limit(power, -0.6, 1.0)
        mBrakeMode = true
        setpoint = limitedPower
    }

    public fun setPositionRaw(ticks: Int) {
        if (!zeroed) return
        mBrakeMode = true
        val positionTicks = Utils.limit(
            ticks.toDouble(),
            Constants.Lift.MIN_ENCODER_TICKS.toDouble(),
            Constants.Lift.MAX_ENCODER_TICKS.toDouble()
        )
        mElevatorMode = ElevatorMode.MOTION_MAGIC
        setpoint = positionTicks
        mMaster.set(ControlMode.MotionMagic, setpoint)
    }

    public fun setPosition(positionInches: Double) {
        val positionTicks = Utils.inchesToEncoderTicks(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            positionInches,
            Constants.Lift.ENCODER_REDUCTION
        )
        setPositionRaw(positionTicks.toInt())
    }

    public fun setCarriagePosition(positionInches: Double) {
        setPosition(0.5 * positionInches)
    }

    public fun setIntakeHeight(height: LiftHeight) {
        // Logger.tag("LIFT_HEIGHT").trace(height)
        setCarriagePosition(height.carriageHeightInches())
    }

    public fun setVelocityRaw(ticksPer100ms: Int) {
        if (!zeroed) return
        mBrakeMode = false
        val speed = Utils.limit(ticksPer100ms.toDouble(), Constants.Lift.MAX_VELOCITY_SETPOINT.toDouble())
        mElevatorMode = ElevatorMode.VELOCITY
        setpoint = speed
        mMaster.set(ControlMode.Velocity, setpoint)
    }

    public fun setVelocity(inchesPerSecond: Double) {
        val speed = Utils.inchesPerSecondToEncoderTicksPer100Ms(
            Constants.Lift.ENCODER_TICKS_PER_ROTATION,
            Constants.Lift.SPROCKET_CIR,
            inchesPerSecond,
            Constants.Lift.ENCODER_REDUCTION
        )
        setVelocityRaw(speed.toInt())
    }

    public fun setCarriageVelocity(inchesPerSecond: Double) {
        setVelocity(inchesPerSecond * 0.5)
    }

    public override fun update() {
        if (!zeroed) {
            mElevatorMode = ElevatorMode.ZERO
            mSoftLimitsEnabled = false
            if (mFirstLoop) {
                super.timer.stop()
                super.timer.reset()
                super.timer.start()
                mFirstLoop = false
            }
        }
        when (mElevatorMode) {
            ElevatorMode.ZERO -> {
                // drive downwards until hall effect detects carriage
                if (
                    super.timer.get() > Constants.Lift.ZEROING_TIMEOUT &&
                    Math.abs(firstStageVelocityRaw) < Constants.Lift.ZEROING_THRESHOLD
                ) {
                    zeroed = true
                    mMaster.set(ControlMode.PercentOutput, 0.0)
                    setpoint = 0.0
                    setZero()
                    mElevatorMode = ElevatorMode.OPEN_LOOP
                    mSoftLimitsEnabled = true
                }
                mMaster.set(ControlMode.PercentOutput, Constants.Lift.ZEROING_SPEED)
            }
            ElevatorMode.VELOCITY -> {
                mSoftLimitsEnabled = true
                mMaster.set(ControlMode.Velocity, setpoint)
            }
            ElevatorMode.OPEN_LOOP -> {
                mSoftLimitsEnabled = false
                mMaster.set(ControlMode.PercentOutput, setpoint)
            }
            ElevatorMode.MOTION_MAGIC -> {
                mSoftLimitsEnabled = true
                mMaster.set(ControlMode.MotionMagic, setpoint)
            }
        }
    }

    public override fun stop() {
        mBrakeMode = true
        setPower(0.0)
        setpoint = 0.0
        mMaster.neutralOutput()
    }

    public override fun reset() {
        stop()
    }
}
