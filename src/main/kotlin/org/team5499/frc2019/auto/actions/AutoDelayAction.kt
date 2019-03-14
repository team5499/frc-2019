package org.team5499.frc2019.auto.actions

import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.util.time.ITimer
import org.team5499.monkeyLib.util.time.WPITimer

import org.team5499.frc2019.Constants

public class AutoDelayAction(val timer: ITimer = WPITimer()) : Action(0.0, timer) {

    private var mTimeout: Double

    init {
        mTimeout = Constants.Auto.AUTO_DELAY
    }

    public override fun start() {
        mTimeout = Constants.Auto.AUTO_DELAY
        timer.stop()
        timer.reset()
        timer.start()
    }

    public override fun next(): Boolean {
        return timer.get() > mTimeout
    }

    public override fun finish() {
    }
}
