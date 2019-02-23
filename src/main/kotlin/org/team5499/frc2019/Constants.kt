package org.team5499.frc2019

import org.team5499.dashboard.DashboardVar

@SuppressWarnings("MagicNumber")
object Constants {
    fun initConsts() {
        DashboardVar.initClassProps(Constants::class)
    }
    public var TEST_KP by DashboardVar(2.0)
    public var TEST_KI by DashboardVar(1.0)
    public var TEST_KD by DashboardVar(1.5)
}
