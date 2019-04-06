package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.Subsystem
import org.team5499.frc2019.Constants

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.ControlMode

import org.team5499.dashboard.Dashboard

public class HatchMech(talon: LazyTalonSRX) : Subsystem() {

    public enum class HatchMechPosition(val ticks: () -> Int) {
        TOP_STOW({ Constants.Hatch.TOP_STOW_POSITION }),
        BOTTOM_STOW({ Constants.Hatch.BOTTOM_STOW_POSITION }),
        DEPLOYED({ Constants.Hatch.DEPLOY_POSITION }),
        HOLD({ Constants.Hatch.HOLD_POSITION })
    }

    private val mTalon: LazyTalonSRX

    private var mPositionOffset: Int

    public val positionRaw: Int
        get() = mTalon.getSelectedSensorPosition(0)

    // private var mDisabled: Boolean

    public var selectedPosition: HatchMechPosition = HatchMechPosition.BOTTOM_STOW
        private set

    init {
        mTalon = talon.apply {
            setNeutralMode(NeutralMode.Brake)
            configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 0)
            config_kP(0, Constants.Hatch.KP, 0)
            config_kI(0, Constants.Hatch.KI, 0)
            config_kD(0, Constants.Hatch.KD, 0)
            config_kF(0, 0.0, 0)
        }
        mTalon.neutralOutput()
        mPositionOffset = Constants.Hatch.POSITION_OFFSET
        // mDisabled = false

        Dashboard.addInlineListener("Constants.Hatch.KP") {
            _: String, value: Double? ->
            if (value != null)
                mTalon.config_kP(0, value, 0)
        }
        Dashboard.addInlineListener("Constants.Hatch.KI") {
            _: String, value: Double? ->
            if (value != null)
                mTalon.config_kP(0, value, 0)
        }
        Dashboard.addInlineListener("Constants.Hatch.KD") {
            _: String, value: Double? ->
            if (value != null)
                mTalon.config_kP(0, value, 0)
        }

        Dashboard.addInlineListener("Constants.Hatch.POSITION_OFFSET") {
            _: String, value: Int? ->
            if (value != null) {
                mPositionOffset = value
            }
        }
    }

    public fun setPositionRaw(ticks: Int) {
        mPositionOffset = Constants.Hatch.POSITION_OFFSET
        mTalon.set(ControlMode.Position, (mPositionOffset + ticks).toDouble())
    }

    public fun setPosition(position: HatchMechPosition) {
        selectedPosition = position
        setPositionRaw(position.ticks())
    }

    // call to turn off hatchmech
    public fun disable() {
        // mDisabled = true
        timer.stop()
        timer.reset()
        timer.start()
    }

    public override fun update() {
        // println("hatchmech pos: ${mTalon.getSelectedSensorPosition(0)}")
        // @Suppress("MagicNumber")
        // if (mDisabled && timer.get() < 2.0) {
        //     mTalon.set(ControlMode.PercentOutput, 0.5)
        // } else if (mDisabled) {
        //     mTalon.set(ControlMode.PercentOutput, 0.0)
        // }
    }

    public override fun stop() {
        mTalon.neutralOutput()
    }

    public override fun reset() {
        stop()
        // mDisabled = false
    }
}
