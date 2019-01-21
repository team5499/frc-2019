package org.team5499.frc2019.controllers

import org.team5499.monkeyLib.Controller

import org.team5499.frc2019.subsystems.SubsystemsManager

public class AutoController(subsystems: SubsystemsManager) : Controller() {

    val mSubsystems: SubsystemsManager

    init {
        mSubsystems = subsystems
    }

    public override fun start() {
    }

    public override fun update() {
    }

    public override fun reset() {
    }
}
