package org.team5499.frc2019.auto

import org.team5499.monkeyLib.auto.Routine
import org.team5499.monkeyLib.auto.NothingAction
import org.team5499.monkeyLib.math.geometry.Rotation2d

import org.team5499.frc2019.subsystems.SubsystemsManager

@SuppressWarnings("MagicNumber")
public class Routines(paths: Paths, subsystems: SubsystemsManager) {

    private val mPaths: Paths
    private val mSubsystems: SubsystemsManager

    public val baseline: Routine

    init {
        mPaths = paths
        mSubsystems = subsystems

        this.baseline = createBaseline()
    }

    private fun createBaseline(): Routine {
        return Routine("baseline", Rotation2d.fromDegrees(0),
            NothingAction(10.0)
        )
    }

    public fun resetAll() {
        baseline.reset()
    }
}
