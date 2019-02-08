package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode

import org.team5499.frc2019.Constants

public class Intake(talon: LazyTalonSRX) : Subsystem() {

    companion object {
        private const val kBallDetectionThreshold = 12 // amps
    }

    public enum class IntakeMode(val percent: Double) {
        INTAKE(Constants.Intake.INTAKE_SPEED),
        HOLD(Constants.Intake.HOLD_SPEED),
        OUTTAKE(Constants.Intake.OUTTAKE_SPEED),
        IDLE(Constants.Intake.IDLE_SPEED)
    }

    private val mTalon: LazyTalonSRX

    private var mMode: IntakeMode

    public val ballInIntake: Boolean
        get() = mTalon.getOutputCurrent() > kBallDetectionThreshold

    init {
        mTalon = talon
        mTalon.configSelectedFeedbackSensor(FeedbackDevice.Analog)
        mTalon.setNeutralMode(NeutralMode.Coast)

        mMode = IntakeMode.IDLE
    }

    public fun intake() {
        mMode = IntakeMode.INTAKE
    }

    public fun outtake() {
        mMode = IntakeMode.OUTTAKE
    }

    public fun idle() {
        mMode = IntakeMode.IDLE
    }

    public fun hold() {
        mMode = IntakeMode.HOLD
    }

    public override fun update() {
        when (mMode) {
            IntakeMode.INTAKE -> {
                // if (ballInIntake) {
                //     mMode = IntakeMode.HOLD
                // }
            }
            IntakeMode.OUTTAKE -> {}
            IntakeMode.HOLD -> {}
            IntakeMode.IDLE -> {}
        }
        mTalon.set(ControlMode.PercentOutput, mMode.percent)
    }

    public override fun stop() {
        mTalon.set(ControlMode.PercentOutput, 0.0)
        mTalon.neutralOutput()
        mMode = IntakeMode.IDLE
    }

    public override fun reset() {
        stop()
    }
}
