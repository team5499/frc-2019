package org.team5499.frc2019.input

public interface IDriverControls {

    public fun getThrottle(): Double

    public fun getTurn(): Double

    // for tank only with joysticks
    public fun getLeft(): Double

    public fun getRight(): Double

    // other methods

    public fun getQuickTurn(): Boolean

    public fun getCreep(): Boolean
}
