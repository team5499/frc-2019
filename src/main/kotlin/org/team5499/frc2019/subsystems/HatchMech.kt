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

    private var mLatchState: LatchState = LatchState.OPEN

    public val latchPositionRaw: Int
        get() = mHookMotor.getSensorCollection().getAnalogInRaw()

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
            // config_kP()
            // config_kI()
            // config_kD()
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
        // if (shouldOpen) {
        //     @Suppress("MagicNumber")
        //     mHookMotor.set(ControlMode.PercentOutput, 1.0)
        // } else {
        //     @Suppress("MagicNumber")
        //     mHookMotor.set(ControlMode.PercentOutput, -1.0)
        // }
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
