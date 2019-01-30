package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.math.physics.DCMotorTransmission

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode

import org.team5499.frc2019.Constants

import edu.wpi.first.wpilibj.Timer

public class Intake(talon: LazyTalonSRX) : Subsystem() {

    public enum class IntakeMode(val percent: Double) {
        INTAKE(Constants.Intake.INTAKE_SPEED),
        HOLD(Constants.Intake.HOLD_SPEED),
        OUTTAKE(Constants.Intake.OUTTAKE_SPEED)
    }

    private val mTalon: LazyTalonSRX

    private var mMode: IntakeMode
    private val mTransmission: DCMotorTransmission
    private val mTimer: Timer

    private var mLastAngularVelocity: Double
    private var mLastTime: Double

    init {
        mTalon = talon
        mTalon.setNeutralMode(NeutralMode.Coast)

        mTransmission = DCMotorTransmission(-1.0, -1.0, -1.0) // check these numbers

        mMode = IntakeMode.HOLD

        mTimer = Timer()
        mLastAngularVelocity = 0.0
        mLastTime = 0.0
    }

    public fun intake() {
        mMode = IntakeMode.INTAKE
    }

    public fun outtake() {
        mMode = IntakeMode.OUTTAKE
    }

    public fun hold() {
        mMode = IntakeMode.HOLD
    }

    private fun checkForHold(deltaTime: Double): Boolean {
        return true
    }

    public override fun update() {
        val now = Timer.get()
        val deltaTime = now - mLastTime
        mLastTime = now

        when (mMode) {
            IntakeMode.INTAKE -> {
                if (checkForHold(deltaTime)) {
                    mMode = IntakeMode.HOLD
                }
            }
            IntakeMode.OUTTAKE -> {}
            IntakeMode.HOLD -> {}
        }
        mTalon.set(ControlMode.PercentOutput, mMode.percent)
    }

    public override fun stop() {
        mTalon.set(ControlMode.PercentOutput, 0.0)
        mTalon.neutralOutput()
    }

    public override fun reset() {
        stop()
        mLastTime = 0.0
        mLastAngularVelocity = 0.0
    }
}
