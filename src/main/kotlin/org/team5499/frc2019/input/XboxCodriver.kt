package org.team5499.frc2019.input

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import org.team5499.monkeyLib.input.ButtonState

@Suppress("TooManyFunctions")
public class XboxCodriver(xbox: XboxController) : ICodriverControls {

    companion object {
        private const val kTriggerThreshold = 0.05
        private const val kDpadThreshold = 30
    }

    private val mXbox: XboxController

    init {
        mXbox = xbox
    }

    public override fun getManualInput() = mXbox.getY(Hand.kRight)

    public override fun getStowElevator() = mXbox.getXButtonPressed()

    public override fun getHatchLow() = mXbox.getAButtonPressed()

    public override fun getHatchMid() = mXbox.getBButtonPressed()

    public override fun getHatchHigh() = mXbox.getYButtonPressed()

    // dpad is not accurate so need to add tolerances
    // down on dpad
    @Suppress("MagicNumber")
    public override fun getBallLow(): Boolean {
        val pov = mXbox.getPOV()
        return pov >= 150 && pov <= 210
    }

    // right on dpad
    @Suppress("MagicNumber")
    public override fun getBallMid(): Boolean {
        val pov = mXbox.getPOV()
        return pov >= 90 && pov <= 120
    }

    // up on dpad
    @Suppress("MagicNumber")
    public override fun getBallHigh(): Boolean {
        val pov = mXbox.getPOV()
        return pov >= 330 && pov <= 30
    }

    @Suppress("MagicNumber")
    public override fun getBallHumanPlayer(): Boolean {
        val pov = mXbox.getPOV()
        return pov >= 240 && pov <= 300
    }

    public override fun getIntake() = mXbox.getBumperPressed(Hand.kLeft)

    public override fun getExaust() = mXbox.getBumperPressed(Hand.kRight)

    public override fun getPickup() = ButtonState(
        mXbox.getTriggerAxis(Hand.kLeft) > kTriggerThreshold,
        false, false
    )

    public override fun getPlace() = ButtonState(
        mXbox.getTriggerAxis(Hand.kRight) > kTriggerThreshold,
        false, false
    )

    public override fun getManualEnable() = mXbox.getBackButtonPressed()

    public override fun getManualZero() = false

    public override fun getDisableHatch() = false
}
