package org.team5499.frc2019.controllers

import edu.wpi.first.wpilibj.GenericHID.Hand
import edu.wpi.first.wpilibj.XboxController

import org.team5499.monkeyLib.Controller

import kotlin.math.abs

fun XboxController.anyButtonPressed(): Boolean {
    @Suppress("MagicNumber")
    val threshold = 0.05
    return (
        this.getAButtonPressed() ||
        this.getBButtonPressed() ||
        this.getYButtonPressed() ||
        this.getXButtonPressed() ||
        this.getBumperPressed(Hand.kLeft) ||
        this.getBumperPressed(Hand.kRight) ||
        abs(this.getY(Hand.kLeft)) > threshold ||
        abs(this.getX(Hand.kLeft)) > threshold ||
        abs(this.getY(Hand.kRight)) > threshold ||
        abs(this.getX(Hand.kRight)) > threshold ||
        this.getTriggerAxis(Hand.kLeft) > threshold ||
        this.getTriggerAxis(Hand.kRight) > threshold
    )
}

public class SandstormController(
    driver: XboxController,
    codriver: XboxController,
    teleopController: TeleopController,
    autoController: AutoController
) : Controller() {

    private var mInAuto: Boolean
    private var mCurrentController: Controller

    private val mDriver: XboxController
    private val mCodriver: XboxController

    private val mTeleopController: TeleopController
    private val mAutoController: AutoController

    init {
        mInAuto = true

        mDriver = driver
        mCodriver = codriver

        mTeleopController = teleopController
        mAutoController = autoController
        mCurrentController = mAutoController
    }

    public override fun start() {
        mCurrentController = mAutoController
        mCurrentController.start()
    }

    public override fun update() {
        // check for override
        if (!mInAuto && (mDriver.anyButtonPressed() || mCodriver.anyButtonPressed())) {
            mInAuto = true
            mCurrentController = mTeleopController
        }
        // update seleceted controller
        mCurrentController.update()
    }

    public override fun reset() {
        mInAuto = true
        mCurrentController = mAutoController
    }
}
