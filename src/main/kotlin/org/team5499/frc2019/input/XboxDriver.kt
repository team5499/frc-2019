package org.team5499.frc2019.input

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand
import org.team5499.monkeyLib.input.ButtonState

public class XboxDriver(xbox: XboxController) : IDriverControls {

    private val mXbox: XboxController

    init {
        mXbox = xbox
    }

    public override fun getThrottle() = mXbox.getY(Hand.kLeft)

    public override fun getTurn() = mXbox.getX(Hand.kRight)

    public override fun getLeft() = getThrottle()

    public override fun getRight() = mXbox.getY(Hand.kRight)

    public override fun getQuickTurn() = mXbox.getBumper(Hand.kRight)

    @Suppress("MagicNumber")
    public override fun getCreep() = mXbox.getTriggerAxis(Hand.kRight) > 0.3

    public override fun getExitAuto() = mXbox.getAButtonPressed()

    public override fun getStow() = mXbox.getBButtonPressed()

    public override fun getAutoAlign(): ButtonState {
        return ButtonState(
            mXbox.getBumper(Hand.kLeft),
            mXbox.getBumperPressed(Hand.kLeft),
            mXbox.getBumperReleased(Hand.kLeft)
        )
    }
}
