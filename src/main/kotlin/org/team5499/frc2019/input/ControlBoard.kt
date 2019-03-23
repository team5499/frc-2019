package org.team5499.frc2019.input

public class ControlBoard(
    val driverControls: IDriverControls,
    val codriverControls: ICodriverControls
) {

    companion object {
        private const val kThreshold = 0.05
    }

    public fun getDriverButtonPressed(): Boolean {
        return (
            driverControls.getThrottle() > kThreshold ||
            driverControls.getTurn() > kThreshold ||
            driverControls.getLeft() > kThreshold ||
            driverControls.getRight() > kThreshold ||
            driverControls.getQuickTurn() ||
            driverControls.getCreep()
        )
    }

    public fun getCodriverButtonPressed(): Boolean {
        return (
            codriverControls.getStowElevator() ||
            codriverControls.getHatchLow() ||
            codriverControls.getHatchMid() ||
            codriverControls.getHatchHigh() ||
            codriverControls.getBallLow() ||
            codriverControls.getBallMid() ||
            codriverControls.getBallHigh() ||
            codriverControls.getIntake() ||
            codriverControls.getExaust() ||
            codriverControls.getPickup().down ||
            codriverControls.getPlace().down ||
            codriverControls.getManualEnable()
        )
    }

    public fun getAnyButtonPressed() = (getCodriverButtonPressed() || getDriverButtonPressed())
}
