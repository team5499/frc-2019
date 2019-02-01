package org.team5499.frc2019.subsystems

import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.Subsystem

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice

/**
 * Class that represents the mechanism that will grab and place the hatches
 */
public class HatchMech(
    val mArmMotor: LazyTalonSRX,
    val mHookMotor: LazyTalonSRX
) : Subsystem() {

    companion object {
        private const val kLatchAcceptableError = 50 // pot ticks
    }

    @SuppressWarnings("MagicNumber")
    private enum class LatchState(val setpointTicks: Int) {
        OPEN(1000),
        CLOSED(100)
    }

    @SuppressWarnings("MagicNumber")
    public enum class ArmPosition(val setpointTicks: Int) {
        STOWED(100),
        PLACE(1000),
        PICKUP(1200)
    }

    private var mLatchState: LatchState = LatchState.OPEN

    public val latchPositionRaw: Int
        get() = mHookMotor.getSensorCollection().getAnalogInRaw()

    public val armPositionRaw: Int
        get() = mArmMotor.getSensorCollection().getAnalogInRaw()

    public val armVelocityRaw: Int
        get() = mArmMotor.getSensorCollection().getAnalogInVel()

    public val armPositionError: Int
        get() = mArmMotor.getClosedLoopError(0)

    public val isOpen: Boolean
        get() {
            if (Constants.Hatch.OPEN_DETECTOR_NEGATIVE) {
                return (Constants.Hatch.OPEN_DETECTION_LIMIT > mHookMotor.getSensorCollection().getAnalogInRaw())
            } else {
                return mHookMotor.getSensorCollection().getAnalogInRaw() > Constants.Hatch.OPEN_DETECTION_LIMIT
            }
        }

    public fun setArmPercent(precent: Double) {
        mArmMotor.set(ControlMode.PercentOutput, precent)
    }

    init {
        mArmMotor.apply {
            configSelectedFeedbackSensor(FeedbackDevice.Analog)
            config_kP(0, 1.0, 0)
            config_kI(0, 0.0, 0)
            config_kD(0, 0.0, 0)
            config_kF(0, 0.0, 0)
            setSensorPhase(true) // check this
        }

        mHookMotor.apply {
            configSelectedFeedbackSensor(FeedbackDevice.Analog)
            setInverted(Constants.Hatch.OPEN_NEGATIVE)
        }
    }
    @SuppressWarnings("MagicNumber")
    /**
     * Open or close the latch
     */
    public fun setLatchState(shouldOpen: Boolean) {
        mLatchState = if (shouldOpen) LatchState.OPEN else LatchState.CLOSED
    }

    public fun setLatchPosition(armPosition: ArmPosition) {
        mArmMotor.set(ControlMode.Position, armPosition.setpointTicks.toDouble())
    }

    public fun setLatchSetpoint(ticks: Int) {
        mArmMotor.set(ControlMode.Position, ticks.toDouble())
    }

    public override fun update() {
        if (Math.abs(mLatchState.setpointTicks - latchPositionRaw) < kLatchAcceptableError) {
            mHookMotor.set(ControlMode.PercentOutput, 0.0)
        } else if (latchPositionRaw > mLatchState.setpointTicks) {
            mHookMotor.set(ControlMode.PercentOutput, 1.0)
        } else {
            mHookMotor.set(ControlMode.PercentOutput, -1.0)
        }
    }

    public override fun reset() {
        mHookMotor.setInverted(Constants.Hatch.OPEN_NEGATIVE)
    }

    public override fun stop() {
        mArmMotor.set(ControlMode.PercentOutput, 0.0)
    }
}
