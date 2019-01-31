package org.team5499.frc2019.subsystems

import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.Subsystem

import com.ctre.phoenix.motorcontrol.ControlMode

/**
 * Class that represents the mechanism that will grab and place the hatches
 */
public class HatchMech(
    val mArmMotor: LazyTalonSRX,
    val mHookMotor: LazyTalonSRX
) : Subsystem() {
    private enum class HookMode {
        OPEN,
        CLOSED
    }

    public val isOpen: Boolean
        get() {
            if (Constants.Hatch.OPEN_DETECTOR_NEGITIVE) {
                return Constants.Hatch.HATCH_OPEN_DETECTION_LIMIT > mHookMotor.getSensorCollection().getAnalogInRaw()
            } else {
                return mHookMotor.getSensorCollection().getAnalogInRaw() > Constants.Hatch.HATCH_OPEN_DETECTION_LIMIT
            }
        }

    public fun setArmPercent(precent: Double) {
        mArmMotor.set(ControlMode.PercentOutput, precent)
    }

    init {
        mArmMotor.apply {
            config_kP()
        }
    }

    @SuppressWarnings("MagicNumber")
    /**
     * Open or close the latch
     */
    public fun setLatchState(shouldOpen: Boolean) {
        mHookMotor.setInverted(Constants.Hatch.OPEN_NEGIGIVE)

        if (shouldOpen) {
            @Suppress("MagicNumber")
            mHookMotor.set(ControlMode.PercentOutput, 1.0)
        } else {
            @Suppress("MagicNumber")
            mHookMotor.set(ControlMode.PercentOutput, -1.0)
        }
    }

    public override fun update() {
    }

    public override fun reset() {
        mHookMotor.setInverted(Constants.Hatch.OPEN_NEGIGIVE)
    }

    public override fun stop() {
        mArmMotor.set(ControlMode.PercentOutput, 0.0)
    }
}
