package org.team5499.frc2019.auto.actions

import org.team5499.monkeyLib.auto.Action

import org.team5499.frc2019.subsystems.Lift

public class WaitForLiftZeroAction(val lift: Lift) : Action(0.0) {

    public override fun next(): Boolean {
        return lift.zeroed
    }
}
