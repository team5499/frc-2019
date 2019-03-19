package org.team5499.frc2019.controllers

import org.team5499.frc2019.input.ControlBoard

import org.team5499.monkeyLib.Controller

public class SandstormController(
    controlBoard: ControlBoard,
    teleopController: TeleopController,
    autoController: AutoController
) : Controller() {

    private var mInAuto: Boolean
    private var mCurrentController: Controller

    private val mControlBoard: ControlBoard

    private val mTeleopController: TeleopController
    private val mAutoController: AutoController

    init {
        mInAuto = true

        mControlBoard = controlBoard

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
        if (mInAuto && mControlBoard.driverControls.getExitAuto()) {
            mInAuto = false
            mCurrentController = mTeleopController
            mCurrentController.start()
        }
        // update seleceted controller
        mCurrentController.update()
    }

    public override fun reset() {
        mInAuto = true
        mCurrentController = mAutoController
    }
}
