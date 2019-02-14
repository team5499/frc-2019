package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.Subsystem
import org.team5499.frc2019.Constants

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.ControlMode

import org.team5499.dashboard.Dashboard

public class HatchMech(talon: LazyTalonSRX) : Subsystem() {

    @SuppressWarnings("MagicNumber")
    public enum class HatchMechPosition(val ticks: Int) {
        TOP_STOW(125),
        BOTTOM_STOW(900),
        DEPLOYED(470),
        HOLD(350)
    }

    private val mTalon: LazyTalonSRX

    public val positionRaw: Int
        get() = mTalon.getSelectedSensorPosition(0)

    init {
        mTalon = talon.apply {
            setNeutralMode(NeutralMode.Brake)
            configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 0)
            config_kP(0, Constants.Hatch.HATCH_KP, 0)
            config_kI(0, Constants.Hatch.HATCH_KI, 0)
            config_kD(0, Constants.Hatch.HATCH_KD, 0)
            config_kF(0, 0.0, 0)
        }
        Dashboard.addVarListener("HATCH_KP", {
            key: String, value: Any? ->
            mTalon.config_kP(0, Dashboard.getDouble("HATCH_KP"), 0)
        })
        Dashboard.addVarListener("HATCH_KI", {
            key: String, value: Any? ->
            mTalon.config_kP(0, Dashboard.getDouble("HATCH_KI"), 0)
        })
        Dashboard.addVarListener("HATCH_KD", {
            key: String, value: Any? ->
            mTalon.config_kP(0, Dashboard.getDouble("HATCH_KD"), 0)
        })
    }

    public fun setPosition(position: HatchMechPosition) {
        mTalon.set(ControlMode.Position, position.ticks.toDouble())
    }

    public override fun update() {
        // println("position: $positionRaw ticks")
    }

    public override fun stop() {
        mTalon.neutralOutput()
    }

    public override fun reset() {
        stop()
    }
}
