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
import org.team5499.frc2019.auto.actions.DriveStraightAction
import org.team5499.frc2019.auto.actions.PathAction
import org.team5499.frc2019.auto.actions.LiftAction
import org.team5499.frc2019.auto.actions.HatchMechAction
import org.team5499.frc2019.auto.actions.WaitForLiftZeroAction
import org.team5499.frc2019.auto.actions.CrossedXBoundaryAction

import java.util.HashMap

@SuppressWarnings("MagicNumber")
public class Routines(paths: Paths, subsystems: SubsystemsManager) {

    private val mPaths: Paths
    private val mSubsystems: SubsystemsManager

    public val routineMap: HashMap<String, Routine>

    public val baseline: Routine
    public val tuning: Routine
    public val test: Routine
    public val rocketLeft: Routine
    public val rocketRight: Routine
    public val cargoShipThenRocketRight: Routine

    init {
        mPaths = paths
        mSubsystems = subsystems
        routineMap = HashMap<String, Routine>()

        this.baseline = createBaseline()
        this.tuning = createTuning()
        this.test = createTest()
        this.rocketLeft = createRocketLeft()
        this.rocketRight = createRocketRight()
        this.cargoShipThenRocketRight = createCargoShipThenRocketRight()

        routineMap.put(baseline.name, baseline)
        routineMap.put(tuning.name, tuning)
        routineMap.put(test.name, test)
        routineMap.put(rocketLeft.name, rocketLeft)
        routineMap.put(rocketRight.name, rocketRight)
    }

    public fun getRoutineWithName(name: String): Routine? {
        if (routineMap.containsKey(name)) {
            return routineMap.get(name)
        } else {
            return null
        }
    }

    private fun createRocketLeft() = Routine(
        "left_rocket",
        Paths.Poses.leftStartingPosition.mirror(),
        ParallelAction(
            SerialAction(
                PathAction(15.0, mPaths.fromHabToLeftRocket, mSubsystems.drivetrain)
            ),
            SerialAction(
                WaitForLiftZeroAction(mSubsystems.lift),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift),
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
                WaitForLiftZeroAction(mSubsystems.lift),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift),
                HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
                CrossedXBoundaryAction(90.0, false, mSubsystems.drivetrain),
                LiftAction(LiftHeight.BALL_MID, mSubsystems.lift)
            )
        ),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech),
        NothingAction(0.3),
        ParallelAction(
            PathAction(15.0, mPaths.rightRocketBackup, mSubsystems.drivetrain),
            SerialAction(
                NothingAction(0.5),
                LiftAction(LiftHeight.HATCH_LOW, mSubsystems.lift)
            )
        ),
        PathAction(15.0, mPaths.rightBackupToStation, mSubsystems.drivetrain),
        HatchMechAction(HatchMechPosition.HOLD, mSubsystems.hatchMech),
        NothingAction(0.3),
        PathAction(15.0, mPaths.rightRocketBackup2, mSubsystems.drivetrain),
        ParallelAction(
            PathAction(15.0, mPaths.rightRocketTinyBoi, mSubsystems.drivetrain),
            LiftAction(LiftHeight.HATCH_MID, mSubsystems.lift)
        ),
        HatchMechAction(HatchMechPosition.DEPLOYED, mSubsystems.hatchMech),
        DriveStraightAction(5.0, -6.0, mSubsystems.drivetrain)
    )

    private fun createCargoShipThenRocketRight() = Routine(
        "cargoship_then_rocket_right",
        Paths.Poses.rightCargoShipToRocketStartingPosition,
        ParallelAction(
            PathAction(15.0, mPaths.rightHabToFrontCargo, mSubsystems.drivetrain),
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
        "baseline",
        DriveStraightAction(15.0, 50.0, mSubsystems.drivetrain)
    )

    private fun createTuning() = Routine(
        "tuning",
        Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0)),
        PathAction(15.0, mPaths.tuning, mSubsystems.drivetrain)
    )

    private fun createTest() = Routine(
        "test",
        Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0.0)),
        DriveStraightAction(10.0, 12.0, mSubsystems.drivetrain)
    )

    public fun resetAll() {
        baseline.reset()
        tuning.reset()
        test.reset()
        rocketLeft.reset()
        rocketRight.reset()
    }
}
