package org.team5499.frc2019.input

@Suppress("TooManyFunctions")
public interface ICodriverControls {

    // manual control
    public fun getManualInput(): Double

    public fun getManualInputEnabled(): Boolean

    // misc elevator
    public fun getStowElevator(): Boolean

    // hatches
    public fun getHatchLow(): Boolean

    public fun getHatchMid(): Boolean

    public fun getHatchHigh(): Boolean

    // cargo
    public fun getBallLow(): Boolean

    public fun getBallMid(): Boolean

    public fun getBallHigh(): Boolean

    // cargo mech
    public fun getIntake(): Boolean

    public fun getExaust(): Boolean

    // hatch mech
    public fun getPickup(): Boolean

    public fun getPlace(): Boolean

    // ramp (?) maybe this should be on the drivers controller
    public fun getDropRamp(): Boolean
}
