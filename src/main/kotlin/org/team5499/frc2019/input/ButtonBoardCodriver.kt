package org.team5499.frc2019.input

import org.team5499.monkeyLib.input.ButtonState

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
    public override fun getBallHumanPlayer() = mButtonBoard.getRawButtonPressed(5 + 1)

    @Suppress("MagicNumber")
    public override fun getStowElevator() = mButtonBoard.getRawButtonPressed(7 + 1)

    @Suppress("MagicNumber")
    public override fun getHatchLow() = mButtonBoard.getRawButtonPressed(6 + 1)

    @Suppress("MagicNumber")
    public override fun getHatchMid() = mButtonBoard.getRawButtonPressed(11 + 1)

    @Suppress("MagicNumber")
    public override fun getHatchHigh() = mButtonBoard.getRawButtonPressed(8 + 1)

    @Suppress("MagicNumber")
    public override fun getBallLow() = mButtonBoard.getRawButtonPressed(4 + 1)

    @Suppress("MagicNumber")
    public override fun getBallMid() = mButtonBoard.getRawButtonPressed(9 + 1)

    @Suppress("MagicNumber")
    public override fun getBallHigh() = mButtonBoard.getRawButtonPressed(10 + 1)

    @Suppress("MagicNumber")
    public override fun getIntake() = mButtonBoard.getRawButton(2 + 1)

    @Suppress("MagicNumber")
    public override fun getExaust() = mButtonBoard.getRawButton(0 + 1)

    @Suppress("MagicNumber")
    public override fun getPickup() = ButtonState(
        mButtonBoard.getRawButton(3 + 1),
        mButtonBoard.getRawButtonPressed(3 + 1),
        mButtonBoard.getRawButtonReleased(3 + 1)
    )

    @Suppress("MagicNumber")
    public override fun getPlace() = ButtonState(
        mButtonBoard.getRawButton(1 + 1),
        mButtonBoard.getRawButtonPressed(1 + 1),
        mButtonBoard.getRawButtonReleased(1 + 1)
    )

    @Suppress("MagicNumber")
    public override fun getDropRamp() = false
}
