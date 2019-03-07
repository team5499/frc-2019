package org.team5499.frc2019

import org.team5499.dashboard.DashboardVar
import org.team5499.dashboard.StringChooser

@SuppressWarnings("MagicNumber")
object Constants {
    fun initProps() {
        println("init Constants")
    }

    public var AUTO_MODE = StringChooser("Constants.AUTO_MODE", "LEFT", "LEFT",
                                                                        "MIDDLE",
                                                                        "RIGHT")

    object Lift {
        fun initProps() {
            println("init Lift")
        }
        public var KP by DashboardVar(2.0)
        public var KI by DashboardVar(1.0)
        public var KD by DashboardVar(1.5)

        public var LOW_HEIGHT by DashboardVar(12.5)
    }
}
