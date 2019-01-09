package org.team5499.frc2019.subsystems

import com.ctre.phoenix.motorcontrol.can.VictorSPX
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.motorcontrol.ControlMode

import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX

public class Drivetrain : Subsystem() {

    private val mLeftMaster: LazyTalonSRX
    private val mLeftSlave1: VictorSPX
    private val mLeftSlave2: VictorSPX

    private val mRightMaster: LazyTalonSRX
    private val mRightSlave1: VictorSPX
    private val mRightSlave2: VictorSPX

    private val mGyro: PigeonIMU

    init {
        mLeftMaster = LazyTalonSRX(Constants.HardwarePorts.LEFT_DRIVE_MASTER)
        mLeftSlave1 = VictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE1)
        mLeftSlave2 = VictorSPX(Constants.HardwarePorts.LEFT_DRIVE_SLAVE2)

        mLeftSlave1.follow(mLeftMaster)
        mLeftSlave2.follow(mLeftMaster)

        mLeftMaster.setInverted(false)

        mRightMaster = LazyTalonSRX(Constants.HardwarePorts.RIGHT_DRIVE_MASTER)
        mRightSlave1 = VictorSPX(Constants.HardwarePorts.RIGHT_DRIVE_SLAVE1)
        mRightSlave2 = VictorSPX(Constants.HardwarePorts.RIGHT_DRIVE_SLAVE2)

        mRightSlave1.follow(mRightMaster)
        mRightSlave2.follow(mRightMaster)

        mRightMaster.setInverted(true)

        mGyro = PigeonIMU(Constants.HardwarePorts.GYRO_PORT)
    }

    public fun setPercent(left: Double, right: Double) {
        mLeftMaster.set(ControlMode.PercentOutput, left)
        mRightMaster.set(ControlMode.PercentOutput, right)
    }

    public override fun update() {
    }

    public override fun stop() {
    }

    public override fun reset() {
    }
}
