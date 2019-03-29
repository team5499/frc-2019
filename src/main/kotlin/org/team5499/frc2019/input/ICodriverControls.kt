package org.team5499.frc2019.input

import org.team5499.monkeyLib.input.ButtonState

@Suppress("TooManyFunctions")
public interface ICodriverControls {

    // manual control
    public fun getManualInput(): Double

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

    public fun getBallHumanPlayer(): Boolean

    // cargo mech
    public fun getIntake(): Boolean

    public fun getExaust(): Boolean

    // hatch mech
    public fun getPickup(): ButtonState

    public fun getPlace(): ButtonState

    // ramp (?) maybe this should be on the drivers controller
    public fun getManualEnable(): Boolean

    public fun getManualZero(): Boolean

    public fun getDisableHatch(): Boolean

    // get the current codriver state
    public fun getState(): String {
        @Suppress("MaxLineLength")
        return "${getManualInput()} : ${getStowElevator()} : ${getHatchLow()} : ${getHatchMid()} : ${getHatchHigh()} : ${getBallLow()} : ${getBallMid()} : ${getBallHigh()} : ${getBallHumanPlayer()} : ${getIntake()} : ${getExaust()} : ${getPickup()} : ${getPlace()}"
    }
}
