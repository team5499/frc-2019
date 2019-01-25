package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX

import org.team5499.frc2019.Constants

public class Intake : Subsystem() {

    private val mPlaceHolderTalon: LazyTalonSRX

    init {
        mPlaceHolderTalon = LazyTalonSRX(Constants.HardwarePorts.INTAKE_MASTER)
    }

    public override fun update() {
    }

    public override fun stop() {
    }

    public override fun reset() {
    }
}
