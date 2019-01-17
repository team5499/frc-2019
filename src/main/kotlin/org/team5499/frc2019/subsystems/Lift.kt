package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.util.Utils

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice

import edu.wpi.first.wpilibj.DigitalInput

@SuppressWarnings("MagicNumber")
public class Lift(masterTalon: LazyTalonSRX /*, slaveTalon: LazyTalonSRX*/, zeroSensor: DigitalInput) : Subsystem() {

    companion object {
        private const val kElevatorSlot = 0
        private const val kTicksPerInch = 1024 // check this
        public const val kMaxElevatorTicks = 8300 // check this
        public const val kMinElevatorTicks = 50 // check this
    }

    public enum class ElevatorMode {
        OPEN_LOOP,
        MOTION_MAGIC,
        ZERO
    }

    private val mMaster: LazyTalonSRX
    // private val mSlave: LazyTalonSRX
    private val mZeroSensor: DigitalInput

    private var mElevatorMode: ElevatorMode

    // private var mEncoderPresent: Boolean
    private var mZeroed: Boolean
    private var mSetpoint: Double

    public val positionTicks: Int
        get() = mMaster.getSelectedSensorPosition(0)

    public val positionInches: Double // inches
        get() = (positionTicks / kTicksPerInch.toDouble()).toDouble()

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

        /*
        this.mSlave = slaveTalon.apply {
            follow(mMaster)
            setInverted(InvertType.FollowMaster)
        }
        */

        this.mZeroSensor = zeroSensor

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
        val positionTicks = Utils.limit(ticks.toDouble(), kMinElevatorTicks.toDouble(), kMaxElevatorTicks.toDouble())
        mElevatorMode = ElevatorMode.MOTION_MAGIC
        mSetpoint = positionTicks
    }

    public fun setPosition(positionInches: Double) {
        val positionTicks = positionInches * kTicksPerInch
        setPositionRaw(positionTicks.toInt())
    }

    public override fun update() {
        // mEncoderPresent = mMaster.getSensorCollection().getPulseWidthRiseToRiseUs() != 0
        if (!mZeroed) mElevatorMode = ElevatorMode.ZERO
        when (mElevatorMode) {
            ElevatorMode.ZERO -> {
                // drive downwards until hall effect detects carriage
                if (mZeroed || mZeroSensor.get()) {
                    mZeroed = true
                    mMaster.set(ControlMode.PercentOutput, 0.0)
                    mSetpoint = 0.0
                    setZero()
                    mElevatorMode = ElevatorMode.OPEN_LOOP
                }
                mMaster.set(ControlMode.PercentOutput, -0.05)
            }
            ElevatorMode.OPEN_LOOP -> {
                mMaster.set(ControlMode.PercentOutput, mSetpoint)
            }
            ElevatorMode.MOTION_MAGIC -> {
                mMaster.set(ControlMode.MotionMagic, mSetpoint)
            }
        }
        // println("elevator position target: ${mMaster.getClosedLoopTarget(0)}")
        // println("elevator position error: ${mMaster.getClosedLoopError(0)}")
        // println("elevator position position: ${mMaster.getSensorCollection().getQuadraturePosition()}")
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
