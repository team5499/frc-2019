package org.team5499.frc2019.controllers

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.auto.Routine
import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.math.geometry.Rotation2d

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.auto.Routines

public class AutoController(subsystems: SubsystemsManager, routines: Routines) : Controller() {

    private val mSubsystems: SubsystemsManager
    private val mRoutines: Routines

    private var currentRoutine: Routine
    private var currentAction: Action?

    private var isFinished: Boolean

    init {
        mSubsystems = subsystems
        mRoutines = routines

        isFinished = false
        currentRoutine = mRoutines.baseline
        currentAction = null
    }

    public override fun start() {
        // TODO choose routine from dashboard
        println("auto controller starting")
        reset()
        currentRoutine = mRoutines.rocketRight
        mSubsystems.drivetrain.brakeMode = true
        mSubsystems.drivetrain.heading = Rotation2d(currentRoutine.startPose.rotation)
        mSubsystems.drivetrain.setPosition(currentRoutine.startPose.translation)
        currentAction = currentRoutine.getCurrentAction()
        currentAction!!.start()
    }

    public override fun update() {
        if (isFinished) {
            return
        }
        if (currentRoutine.isLastStep() && currentAction!!.next()) {
            currentAction!!.finish()
            isFinished = true
            return
        }
        if (currentAction == null) {
            currentAction = currentRoutine.getCurrentAction()
            currentAction!!.start()
        } else if (currentAction!!.next()) {
            currentAction!!.finish()
            currentRoutine.advanceRoutine()
            currentAction = currentRoutine.getCurrentAction()
            currentAction!!.start()
        } else {
            currentAction!!.update()
        }
    }

    public override fun reset() {
        currentAction = null
        currentRoutine = mRoutines.baseline
        mRoutines.resetAll()
        isFinished = false
    }
}
