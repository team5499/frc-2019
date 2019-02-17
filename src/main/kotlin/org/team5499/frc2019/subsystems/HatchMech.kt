package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.Subsystem
import org.team5499.frc2019.Constants

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.ControlMode

public class HatchMech(talon: LazyTalonSRX) : Subsystem() {

    public enum class HatchMechPosition(var ticks: Int) {
        TOP_STOW(Constants.Hatch.TOP_STOW_POSITION),
        BOTTOM_STOW(Constants.Hatch.BOTTOM_STOW_POSITION),
        DEPLOYED(Constants.Hatch.DEPLOY_POSITION),
        HOLD(Constants.Hatch.HOLD_POSITION)
    }

    private val mTalon: LazyTalonSRX

    private var mPositionOffset: Int

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
        mTalon.neutralOutput()
        mPositionOffset = Constants.Hatch.POSITION_OFFSET

        // Dashboard.addVarListener("HATCH_KP", {
        //     _: String, _: Any? ->
        //     mTalon.config_kP(0, Dashboard.getDouble("HATCH_KP"), 0)
        // })
        // Dashboard.addVarListener("HATCH_KI", {
        //     _: String, _: Any? ->
        //     mTalon.config_kP(0, Dashboard.getDouble("HATCH_KI"), 0)
        // })
        // Dashboard.addVarListener("HATCH_KD", {
        //     _: String, _: Any? ->
        //     mTalon.config_kP(0, Dashboard.getDouble("HATCH_KD"), 0)
        // })

        // Dashboard.addVarListener("TOP_STOW_POSITION", {
        //     _: String, _: Any? ->
        //     HatchMechPosition.TOP_STOW.ticks = Dashboard.getInt("TOP_STOW_POSITION")
        // })

        // Dashboard.addVarListener("BOTTOM_STOW_POSITION", {
        //     _: String, _: Any? ->
        //     HatchMechPosition.BOTTOM_STOW.ticks = Dashboard.getInt("BOTTOM_STOW_POSITION")
        // })

        // Dashboard.addVarListener("HOLD_POSITION", {
        //     _: String, _: Any? ->
        //     HatchMechPosition.HOLD.ticks = Dashboard.getInt("HOLD_POSITION")
        // })

        // Dashboard.addVarListener("DEPLOY_POSITION", {
        //     _: String, _: Any? ->
        //     HatchMechPosition.DEPLOYED.ticks = Dashboard.getInt("DEPLOY_POSITION")
        // })

        // Dashboard.addVarListener("POSITION_OFFSET", {
        //     _: String, _: Any? ->
        //     mPositionOffset = Dashboard.getInt("POSITION_OFFSET")
        // })
    }

    public fun setPosition(position: HatchMechPosition) {
        mTalon.set(ControlMode.Position, (mPositionOffset + position.ticks).toDouble())
    }

    public override fun update() {
        // println("hatchmech pos: ${mTalon.getSelectedSensorPosition(0)}")
    }

    public override fun stop() {
        mTalon.neutralOutput()
    }

    public override fun reset() {
        stop()
    }
}
