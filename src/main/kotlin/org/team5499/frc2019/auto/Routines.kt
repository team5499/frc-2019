package org.team5499.frc2019.auto

import org.team5499.monkeyLib.auto.Routine
import org.team5499.monkeyLib.auto.SerialAction
import org.team5499.monkeyLib.auto.NothingAction
import org.team5499.monkeyLib.auto.ParallelAction
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Vector2
import org.team5499.monkeyLib.math.geometry.Pose2d

import org.team5499.frc2019.subsystems.SubsystemsManager
import org.team5499.frc2019.subsystems.Lift.LiftHeight
import org.team5499.frc2019.subsystems.HatchMech.HatchMechPosition
import org.team5499.frc2019.auto.actions.PathAction
import org.team5499.frc2019.auto.actions.LiftAction
import org.team5499.frc2019.auto.actions.HatchMechAction
import org.team5499.frc2019.auto.actions.WaitForLiftZeroAction
import org.team5499.frc2019.auto.actions.CrossedXBoundaryAction
import org.team5499.frc2019.auto.actions.TurnAction
import org.team5499.frc2019.auto.actions.VisionGoalAction
import org.team5499.frc2019.auto.actions.VisionGoalAction.VisionGoal

import java.util.LinkedHashMap

@SuppressWarnings("MagicNumber", "TooManyFunctions")
public class Routines(paths: Paths, subsystems: SubsystemsManager) {

    private val mPaths: Paths
    private val mSubsystems: SubsystemsManager

    public val routineMap: LinkedHashMap<String, Routine>

    public val baseline: Routine
    public val tuning: Routine
    public val test: Routine
    public val rocketLeftBlue: Routine
    public val rocketRightBlue: Routine
    public val rocketLeftRed: Routine
    public val rocketRightRed: Routine
    public val doubleCargoshipRight: Routine
    public val doubleCargoshipLeft: Routine
    public val nothing: Routine

    init {
        mPaths = paths
        mSubsystems = subsystems
        routineMap = LinkedHashMap<String, Routine>()

        this.baseline = createBaseline()
        this.tuning = createTuning()
        this.test = createTest()
        this.rocketLeftBlue = createRocketLeftBlue()
        this.rocketRightBlue = createRocketRightBlue()
        this.rocketLeftRed = createRocketLeftRed()
        this.rocketRightRed = createRocketRightRed()
        this.doubleCargoshipRight = createDoubleCargoHatchRight()
        this.doubleCargoshipLeft = createDoubleCargoHatchLeft()
        this.nothing = createNothing()

        routineMap.put(baseline.name, baseline)
        routineMap.put(rocketLeftBlue.name, rocketLeftBlue)
        routineMap.put(rocketRightBlue.name, rocketRightBlue)
        routineMap.put(rocketLeftRed.name, rocketLeftRed)
        routineMap.put(rocketRightRed.name, rocketRightRed)
        routineMap.put(doubleCargoshipLeft.name, doubleCargoshipLeft)
        routineMap.put(doubleCargoshipRight.name, doubleCargoshipRight)
        routineMap.put(tuning.name, tuning)
        routineMap.put(test.name, test)
        routineMap.put(nothing.name, nothing)
    }

    public fun getRoutineWithName(name: String): Routine? {
        if (routineMap.containsKey(name)) {
            return routineMap.get(name)
        } else {
            return null
        }
    }

    // DONT CHANGE THIS
    private fun createRocketLeftBlue() = Routine(
        "Left Rocket Blue",
        Paths.Poses.leftStartingPosition,
        ParallelAction(
            PathAction(15.0, mPaths.leftRocketSet.get(0), mSubsystems.drivetrain),
            SerialAction(
                WaitForLiftZeroAction(mSubsystems.lift),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift),
                HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
                CrossedXBoundaryAction(115.0, false, mSubsystems.drivetrain),
                LiftAction(LiftHeight.BALL_MID, mSubsystems.lift)
            )
        ),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech),
        NothingAction(0.4),
        ParallelAction(
            PathAction(15.0, mPaths.leftRocketSet.get(1), mSubsystems.drivetrain),
            SerialAction(
                NothingAction(0.5),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift)
            )
        ),
        PathAction(15.0, mPaths.leftRocketSet.get(2), mSubsystems.drivetrain),
        HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
        NothingAction(0.45),
        PathAction(15.0, mPaths.leftRocketSet.get(3), mSubsystems.drivetrain)
        // NothingAction(0.33)
        // ParallelAction(
        //     PathAction(15.0, mPaths.leftRocketSet.get(4), mSubsystems.drivetrain),
        //     LiftAction(LiftHeight.HATCH_MID, mSubsystems.lift)
        // ),
        // HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech)
        // DriveStraightAction(5.0, -6.0, mSubsystems.drivetrain)
    )
    // DONT CHANGE THIS
    private fun createRocketRightBlue() = Routine(
        "Right Rocket Blue",
        Paths.Poses.rightStartingPosition,
        // AutoDelayAction(),
        ParallelAction(
            PathAction(15.0, mPaths.rightRocketSet.get(0), mSubsystems.drivetrain),
            SerialAction(
                WaitForLiftZeroAction(mSubsystems.lift),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift),
                HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
                CrossedXBoundaryAction(115.0, false, mSubsystems.drivetrain),
                LiftAction(LiftHeight.BALL_MID, mSubsystems.lift)
            )
        ),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech),
        NothingAction(0.4),
        ParallelAction(
            PathAction(15.0, mPaths.rightRocketSet.get(1), mSubsystems.drivetrain),
            SerialAction(
                NothingAction(0.5),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift)
            )
        ),
        PathAction(15.0, mPaths.rightRocketSet.get(2), mSubsystems.drivetrain),
        HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
        NothingAction(0.45),
        PathAction(15.0, mPaths.rightRocketSet.get(3), mSubsystems.drivetrain)
        // NothingAction(0.33)
        // ParallelAction(
        //     PathAction(15.0, mPaths.rightRocketSet.get(4), mSubsystems.drivetrain),
        //     LiftAction(LiftHeight.HATCH_MID, mSubsystems.lift)
        // ),
        // HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech)
        // DriveStraightAction(5.0, -6.0, mSubsystems.drivetrain)
    )

    private fun createRocketLeftRed() = Routine(
        "Left Rocket Red",
        Paths.Poses.leftStartingPosition.transformBy(Vector2(-1.5, -4.5)), // 0.0, -2.0
        ParallelAction(
            PathAction(15.0, mPaths.leftRocketSet.get(0), mSubsystems.drivetrain),
            SerialAction(
                WaitForLiftZeroAction(mSubsystems.lift),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift),
                HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
                CrossedXBoundaryAction(115.0, false, mSubsystems.drivetrain),
                LiftAction(LiftHeight.BALL_MID, mSubsystems.lift)
            )
        ),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech),
        NothingAction(0.4),
        ParallelAction(
            PathAction(15.0, mPaths.leftRocketSet.get(1), mSubsystems.drivetrain),
            SerialAction(
                NothingAction(0.5),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift)
            )
        ),
        PathAction(15.0, mPaths.leftRocketSet.get(2), mSubsystems.drivetrain),
        HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
        NothingAction(0.45),
        PathAction(15.0, mPaths.leftRocketSet.get(3), mSubsystems.drivetrain)
    )

    private fun createRocketRightRed() = Routine(
        "Right Rocket Red",
        Paths.Poses.rightStartingPosition.transformBy(Vector2(0.0, 1.5)),
        ParallelAction(
            PathAction(15.0, mPaths.rightRocketSet.get(0), mSubsystems.drivetrain),
            SerialAction(
                WaitForLiftZeroAction(mSubsystems.lift),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift),
                HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
                CrossedXBoundaryAction(115.0, false, mSubsystems.drivetrain),
                LiftAction(LiftHeight.BALL_MID, mSubsystems.lift)
            )
        ),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech),
        NothingAction(0.4),
        ParallelAction(
            PathAction(15.0, mPaths.rightRocketSet.get(1), mSubsystems.drivetrain),
            SerialAction(
                NothingAction(0.5),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift)
            )
        ),
        PathAction(15.0, mPaths.rightRocketSet.get(2), mSubsystems.drivetrain),
        HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
        NothingAction(0.45),
        PathAction(15.0, mPaths.rightRocketSet.get(3), mSubsystems.drivetrain)
    )

    private fun createDoubleCargoHatchRight() = Routine(
        "Double Cargoship Right",
        Paths.Poses.rightCargoShipToRocketStartingPosition,
        // AutoDelayAction(),
        ParallelAction(
            PathAction(15.0, mPaths.rightCargoToRocketSet.get(0), mSubsystems.drivetrain),
            SerialAction(
                WaitForLiftZeroAction(mSubsystems.lift),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift),
                HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech)
            )
        ),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech),
        NothingAction(0.35),
        PathAction(10.0, mPaths.rightCargoToRocketSet.get(1), mSubsystems.drivetrain),
        // NothingAction(0.2),
        PathAction(10.0, mPaths.rightCargoToRocketSet.get(2), mSubsystems.drivetrain),
        HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
        NothingAction(0.33),
        PathAction(10.0, mPaths.rightCargoToRocketSet.get(3), mSubsystems.drivetrain),
        // NothingAction(0.),
        PathAction(10.0, mPaths.rightCargoToRocketSet.get(4), mSubsystems.drivetrain)
    )

    private fun createDoubleCargoHatchLeft() = Routine(
        "Double Cargoship Left",
        Paths.Poses.leftCargoShipToRocketStartingPosition,
        // AutoDelayAction(),
        ParallelAction(
            PathAction(15.0, mPaths.leftCargoToRocketSet.get(0), mSubsystems.drivetrain),
            SerialAction(
                WaitForLiftZeroAction(mSubsystems.lift),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift),
                HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech)
            )
        ),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech),
        NothingAction(0.25)
    )

    private fun createBaseline() = Routine(
        "Baseline",
        Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0.0)),
        ParallelAction(
            PathAction(15.0, mPaths.rightRocketSet.get(0), mSubsystems.drivetrain),
            HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech)
        )
    )

    private fun createTuning() = Routine(
        "Tuning",
        Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0)),
        TurnAction(10.0, 90.0, mSubsystems.drivetrain)
        // PathAction(120.0, mPaths.tuning, mSubsystems.drivetrain)
    )

    private fun createTest() = Routine(
        "test",
        Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0)),
        WaitForLiftZeroAction(mSubsystems.lift),
        LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift),
        HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
        VisionGoalAction(120.0, VisionGoal.HATCH_TARGET, mSubsystems.vision, mSubsystems.drivetrain),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech)
    )

    private fun createNothing() = Routine(
        "Nothing",
        Pose2d(Vector2(0.0, 0.0), Rotation2d.fromDegrees(0.0)),
        HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
        NothingAction(15.0)
    )

    public fun resetAll() {
        baseline.reset()
        tuning.reset()
        test.reset()
        rocketLeftBlue.reset()
        rocketRightBlue.reset()
        rocketLeftRed.reset()
        rocketRightRed.reset()
        doubleCargoshipLeft.reset()
        doubleCargoshipRight.reset()
    }
}
