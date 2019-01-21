package org.team5499.frc2019.input

import edu.wpi.first.wpilibj.Joystick

@Suppress("TooManyFunctions")
@SuppressWarnings("MagicNumber")
public class ButtonBoardCodriver(buttonBoard: Joystick, joystick: Joystick) : ICodriverControls {

    private val mButtonBoard: Joystick
    private val mJoystick: Joystick

    init {
        mButtonBoard = buttonBoard
        mJoystick = joystick
    }

        @Suppress("MagicNumber")
    public override fun getManualInput() = mJoystick.getY()
    @Suppress("MagicNumber")
    public override fun getManualInputEnabled() = mJoystick.getTriggerPressed()
    @Suppress("MagicNumber")
    public override fun getStowElevator() = mButtonBoard.getRawButtonPressed(0)
    @Suppress("MagicNumber")
    public override fun getHatchLow() = mButtonBoard.getRawButtonPressed(1)
    @Suppress("MagicNumber")
    public override fun getHatchMid() = mButtonBoard.getRawButtonPressed(2)
    @Suppress("MagicNumber")
    public override fun getHatchHigh() = mButtonBoard.getRawButtonPressed(3)
    @Suppress("MagicNumber")
    public override fun getBallLow() = mButtonBoard.getRawButtonPressed(4)
    @Suppress("MagicNumber")
    public override fun getBallMid() = mButtonBoard.getRawButtonPressed(5)
    @Suppress("MagicNumber")
    public override fun getBallHigh() = mButtonBoard.getRawButtonPressed(6)
    @Suppress("MagicNumber")
    public override fun getIntake() = mButtonBoard.getRawButtonPressed(7)
    @Suppress("MagicNumber")
    public override fun getExaust() = mButtonBoard.getRawButtonPressed(8)
    @Suppress("MagicNumber")
    public override fun getPickup() = mButtonBoard.getRawButtonPressed(9)
    @Suppress("MagicNumber")
    public override fun getPlace() = mButtonBoard.getRawButtonPressed(10)
    @Suppress("MagicNumber")
    public override fun getDropRamp() = mButtonBoard.getRawButtonPressed(11)
}
