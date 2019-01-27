package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode

import org.team5499.frc2019.Constants

public class Intake : Subsystem() {

    companion object {
        private const val kBallDetectionThreshold = 512 // analog tickss
    }

    public enum class IntakeMode(val percent: Double) {
        INTAKE(Constants.Intake.INTAKE_SPEED),
        HOLD(Constants.Intake.HOLD_SPEED),
        OUTTAKE(Constants.Intake.OUTTAKE_SPEED),
        IDLE(Constants.Intake.IDLE_SPEED)
    }

    private val mTalon: LazyTalonSRX

    private var mMode: IntakeMode

    public val ballDistanceRaw: Int
        get() = mTalon.getSelectedSensorPosition()

    public val ballInIntake: Boolean
        get() = ballDistanceRaw < kBallDetectionThreshold

    init {
        mTalon = LazyTalonSRX(Constants.HardwarePorts.INTAKE_MASTER)
        mTalon.configSelectedFeedbackSensor(FeedbackDevice.Analog)
        mTalon.setNeutralMode(NeutralMode.Coast)

        mMode = IntakeMode.IDLE
    }

    public fun setDesiredIntakeMode(desiredMode: IntakeMode) {
        mMode = desiredMode
    }

    public override fun update() {
        when (mMode) {
            IntakeMode.INTAKE -> {
                if (ballInIntake) {
                    mMode = IntakeMode.HOLD
                }
            }
            IntakeMode.OUTTAKE -> {
                if (!ballInIntake) {
                    mMode = IntakeMode.IDLE
                }
            }
            IntakeMode.HOLD -> {
                if (!ballInIntake) {
                    mMode = IntakeMode.IDLE
                }
            }
            IntakeMode.IDLE -> {
                if (ballInIntake) {
                    mMode = IntakeMode.HOLD
                }
            }
        }
        mTalon.set(ControlMode.PercentOutput, mMode.percent)
    }

    public override fun stop() {
        mTalon.set(ControlMode.PercentOutput, 0.0)
        mTalon.neutralOutput()
        mMode = IntakeMode.IDLE
    }

    public override fun reset() {}
}
