package org.team5499.frc2019.input

import org.team5499.monkeyLib.input.ButtonState

public interface IDriverControls {

    // normal speeds
    public fun getThrottle(): Double

    public fun getTurn(): Double

    // for tank only with joysticks
    public fun getLeft(): Double

    public fun getRight(): Double

    // other methods
    public fun getQuickTurn(): Boolean

    public fun getCreep(): Boolean

    public fun getExitAuto(): Boolean

    public fun getStow(): Boolean

    public fun getAutoAlign(): ButtonState

    public fun getState(): String {
        @Suppress("MaxLineLength")
        return "${getThrottle()} : ${getTurn()} : ${getLeft()} : ${getRight()} : ${getQuickTurn()} : ${getCreep()} : ${getExitAuto()} : ${getStow()}"
    }
}
