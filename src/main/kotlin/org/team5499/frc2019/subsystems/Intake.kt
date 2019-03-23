package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.math.physics.DCMotorTransmission
import org.team5499.monkeyLib.util.CircularDoubleBuffer

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode

import org.team5499.frc2019.Constants

public class Intake(talon: LazyTalonSRX) : Subsystem() {

    companion object {
        public const val kBufferSize = 10
        public const val kSpeedDifferenceThreshold = 20.0 // rads / second
        public const val kCurrentSpeedThreshold = 20.0 // rads /second
        public const val kAccelerationThreshold = 10.0 // rads / s^2
    }

    public enum class IntakeMode(val percent: () -> Double) {
        INTAKE({ Constants.Intake.INTAKE_SPEED }),
        HOLD({ Constants.Intake.HOLD_SPEED }),
        OUTTAKE({ Constants.Intake.OUTTAKE_SPEED })
    }

    private val mTalon: LazyTalonSRX

    private var mMode: IntakeMode
    private val mTransmission: DCMotorTransmission

    private val mVelocityBuffer: CircularDoubleBuffer
    private val mTimeBuffer: CircularDoubleBuffer

    init {
        mTalon = talon
        mTalon.setNeutralMode(NeutralMode.Coast)

        @Suppress("MagicNumber")
        mTransmission = DCMotorTransmission(100.0, 100.0, 100.0, 100.0) // bs numbers

        mMode = IntakeMode.HOLD

        super.timer.start()

        mVelocityBuffer = CircularDoubleBuffer(kBufferSize)
        mTimeBuffer = CircularDoubleBuffer(kBufferSize)

        mVelocityBuffer.add(0.0)
        mTimeBuffer.add(super.timer.get())
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

    private fun checkForHold(): Boolean {
        val voltage = mTalon.getMotorOutputVoltage()
        val current = mTalon.getOutputCurrent()
        val theorheticalSpeed = mTransmission.freeSpeedAtVoltage(voltage)
        val actualSpeed = mTransmission.getSpeedForVoltageAndAmperage(voltage, current)
        val speedDifference = theorheticalSpeed - actualSpeed

        mTimeBuffer.add(super.timer.get())
        mVelocityBuffer.add(actualSpeed)

        val acceleration = (mVelocityBuffer.average) /
            (mTimeBuffer.elements[mTimeBuffer.elements.size - 1] - mTimeBuffer.elements[0])
        return speedDifference > kSpeedDifferenceThreshold &&
            acceleration < kAccelerationThreshold &&
            actualSpeed < kCurrentSpeedThreshold
    }

    public override fun update() {
        val hold = checkForHold()
        when (mMode) {
            IntakeMode.INTAKE -> {
                // if (hold) {
                //     mMode = IntakeMode.HOLD
                // }
            }
            IntakeMode.OUTTAKE -> {}
            IntakeMode.HOLD -> {}
        }
        mTalon.set(ControlMode.PercentOutput, mMode.percent())
    }

    public override fun stop() {
        mTalon.set(ControlMode.PercentOutput, 0.0)
        mTalon.neutralOutput()
    }

    public override fun reset() {
        stop()
        mVelocityBuffer.clear()
        mVelocityBuffer.add(0.0)
        mTimeBuffer.add(super.timer.get())
    }
}
