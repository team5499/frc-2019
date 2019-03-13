package org.team5499.frc2019.auto.actions

import org.team5499.monkeyLib.auto.Action

import org.team5499.frc2019.subsystems.HatchMech
import org.team5499.frc2019.subsystems.HatchMech.HatchMechPosition

public class HatchMechAction(val ticks: Int, val hatchMech: HatchMech) : Action(0.0) {

    public constructor(position: HatchMechPosition, hatchMech: HatchMech):
        this(position.ticks(), hatchMech)

    public override fun start() {
        hatchMech.setPositionRaw(ticks)
    }

    public override fun next() = true
}
