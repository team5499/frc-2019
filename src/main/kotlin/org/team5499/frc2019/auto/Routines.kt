package org.team5499.frc2019.auto

import org.team5499.monkeyLib.auto.Routine
import org.team5499.monkeyLib.math.geometry.Rotation2d

import org.team5499.frc2019.auto.actions.DriveStraightAction
import org.team5499.frc2019.auto.actions.TurnAction
import org.team5499.frc2019.subsystems.SubsystemsManager

@SuppressWarnings("MagicNumber")
public class Routines(paths: Paths, subsystems: SubsystemsManager) {

    private val mPaths: Paths
    private val mSubsystems: SubsystemsManager

    public val baseline: Routine
    public val tuning: Routine

    init {
        mPaths = paths
        mSubsystems = subsystems

        this.baseline = createBaseline()
        this.tuning = createTuning()
    }

    private fun createBaseline() = Routine(
        "baseline",
        Rotation2d.fromDegrees(0),
        DriveStraightAction(15.0, 50.0, mSubsystems.drivetrain)
    )

    private fun createTuning() = Routine(
        "tuning",
        Rotation2d.fromDegrees(0),
        TurnAction(15.0, 90.0, mSubsystems.drivetrain)
    )

    private fun createTest() = Routine(
        "test",
        Rotation2d.fromDegrees(270.0),
        Path
    )

    public fun resetAll() {
        baseline.reset()
    }
}
