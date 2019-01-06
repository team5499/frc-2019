package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX

import org.team5499.frc2019.Constants

public class Lift : Subsystem() {

    private val mMaster: LazyTalonSRX
    private val mSlave: LazyTalonSRX

    init {
        mMaster = LazyTalonSRX(Constants.HardwarePorts.LIFT_MASTER)
        mSlave = LazyTalonSRX(Constants.HardwarePorts.LIFT_SLAVE)
    }

    public override fun update() {
    }

    public override fun stop() {
    }

    public override fun reset() {
    }
}
