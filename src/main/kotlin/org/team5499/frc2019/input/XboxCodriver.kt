package org.team5499.frc2019.input

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

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

    public override fun getManualInputEnabled() = mXbox.getStartButtonPressed()

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

    public override fun getIntake() = mXbox.getBumperPressed(Hand.kLeft)

    public override fun getExaust() = mXbox.getBumperPressed(Hand.kRight)

    public override fun getPickup() = mXbox.getTriggerAxis(Hand.kLeft) > kTriggerThreshold

    public override fun getPlace() = mXbox.getTriggerAxis(Hand.kRight) > kTriggerThreshold

    public override fun getDropRamp() = mXbox.getBackButtonPressed()
}
