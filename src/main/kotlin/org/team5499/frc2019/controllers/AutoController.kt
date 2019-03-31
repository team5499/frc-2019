package org.team5499.frc2019.controllers

import org.team5499.monkeyLib.Controller
import org.team5499.monkeyLib.auto.Routine
import org.team5499.monkeyLib.auto.Action
import org.team5499.monkeyLib.math.geometry.Rotation2d

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.auto.Routines
import org.team5499.frc2019.subsystems.Vision.LEDState
import org.team5499.frc2019.subsystems.Vision.VisionMode

import org.team5499.dashboard.StringChooser

public class AutoController(subsystems: SubsystemsManager, routines: Routines) : Controller() {

    private val mSubsystems: SubsystemsManager
    private val mRoutines: Routines

    private val mAutoSelector: StringChooser
    private val mAllianceSelector: StringChooser

    private var currentRoutine: Routine
    private var currentAction: Action?

    private var isFinished: Boolean

    init {
        mSubsystems = subsystems
        mRoutines = routines

        isFinished = false
        currentRoutine = mRoutines.baseline
        currentAction = null
        val tempArray = Array<String>(mRoutines.routineMap.size, { "" })
        var i = 0
        for ((key, _) in mRoutines.routineMap) {
            tempArray.set(i, key)
            i++
        }

        @Suppress("SpreadOperator")
        mAutoSelector = StringChooser("AUTO_MODE", "baseline", *tempArray)
        mAllianceSelector = StringChooser("ALLIANCE_COLOR", "Blue", "Blue", "Red", "None")
    }

    public override fun start() {
        // TODO choose routine from dashboard
        println("auto controller starting")
        reset()
        val routine = mRoutines.getRoutineWithName(mAutoSelector.selected)
        currentRoutine = if (routine == null) mRoutines.baseline else routine
        // currentRoutine = mRoutines.rocketRight
        mSubsystems.drivetrain.brakeMode = true
        mSubsystems.vision.ledState = LEDState.ON
        mSubsystems.vision.visionMode = VisionMode.VISION
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
