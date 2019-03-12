package org.team5499.frc2019.auto

import org.team5499.monkeyLib.auto.Routine
import org.team5499.monkeyLib.auto.SerialAction
import org.team5499.monkeyLib.auto.NothingAction
import org.team5499.monkeyLib.auto.ParallelAction
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Vector2
import org.team5499.monkeyLib.math.geometry.Pose2d

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Lift.ElevatorHeight
import org.team5499.frc2019.subsystems.HatchMech.HatchMechPosition
import org.team5499.frc2019.auto.actions.DriveStraightAction
import org.team5499.frc2019.auto.actions.PathAction
import org.team5499.frc2019.auto.actions.LiftAction
import org.team5499.frc2019.auto.actions.HatchMechAction
import org.team5499.frc2019.auto.actions.WaitForElevatorZeroAction

@SuppressWarnings("MagicNumber")
public class Routines(paths: Paths, subsystems: SubsystemsManager) {

    private val mPaths: Paths
    private val mSubsystems: SubsystemsManager

    public val baseline: Routine
    public val tuning: Routine
    // public val test: Routine
    public val rocketLeft: Routine
    public val rocketRight: Routine

    init {
        mPaths = paths
        mSubsystems = subsystems

        this.baseline = createBaseline()
        this.tuning = createTuning()
        // this.test = createTest()
        this.rocketLeft = createRocketLeft()
        this.rocketRight = createRocketRight()
    }

    private fun createRocketLeft() = Routine(
        "left_rocket",
        Paths.Poses.leftStartingPosition.mirror(),
        ParallelAction(
            SerialAction(
                PathAction(15.0, mPaths.fromHabToLeftRocket, mSubsystems.drivetrain)
            ),
            SerialAction(
                WaitForElevatorZeroAction(mSubsystems.lift),
                LiftAction(ElevatorHeight.HATCH_LOW, mSubsystems.lift),
                HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech)
            )
        )
    )

    private fun createRocketRight() = Routine(
        "right_rocket",
        Paths.Poses.rightStartingPosition,
        ParallelAction(
            PathAction(15.0, mPaths.fromHabToRightRocket, mSubsystems.drivetrain),
            SerialAction(
                WaitForElevatorZeroAction(mSubsystems.lift),
                LiftAction(ElevatorHeight.HATCH_LOW, mSubsystems.lift),
                HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech)
            )
        ),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech),
        NothingAction(0.75),
        PathAction(15.0, mPaths.rightRocketBackup, mSubsystems.drivetrain),
        PathAction(15.0, mPaths.rightBackupToStation, mSubsystems.drivetrain),
        HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
        NothingAction(0.75),
        PathAction(15.0, mPaths.rightRocketBackup2, mSubsystems.drivetrain)
    )

    private fun createBaseline() = Routine(
        "baseline",
        DriveStraightAction(15.0, 50.0, mSubsystems.drivetrain)
    )

    private fun createTuning() = Routine(
        "tuning",
        Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0)),
        PathAction(15.0, mPaths.tuning, mSubsystems.drivetrain)
    )

    // private fun createTest() = Routine(
    //     "test",
    //     Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0.0)),
    //     )

    public fun resetAll() {
        baseline.reset()
        rocketLeft.reset()
        rocketRight.reset()
    }
}
