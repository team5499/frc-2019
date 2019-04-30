package org.team5499.frc2019.auto

import org.team5499.monkeyLib.path.PathGenerator
import org.team5499.monkeyLib.path.Path
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Vector2
import org.team5499.monkeyLib.path.PathSet

@SuppressWarnings("MagicNumber", "TooManyFunctions")
public class Paths(generator: PathGenerator) {

    private val mGenerator: PathGenerator

    // path sets
    public val rightRocketSet: PathSet

    public val leftRocketSet: PathSet

    public val rightCargoToRocketSet: PathSet

    public val leftCargoToRocketSet: PathSet

    // paths
    public val tuning: Path

    init {
        mGenerator = generator

        rightRocketSet = PathSet()
        leftRocketSet = PathSet()
        rightCargoToRocketSet = PathSet()
        leftCargoToRocketSet = PathSet()

        rightRocketSet.add(generatePathToRightRocket()) // 0
        rightRocketSet.add(generateRightRocketBackup1()) // 1
        rightRocketSet.add(generateRightBackupToStation()) // 2
        rightRocketSet.add(generateRightBackup2()) // 3
        rightRocketSet.add(generateRightRocketTinyBoi()) // 4

        leftRocketSet.add(generatePathToLeftRocket()) // 0
        leftRocketSet.add(generateLeftRocketBackup1()) // 1
        leftRocketSet.add(generateLeftBackupToStation()) // 2
        leftRocketSet.add(generateLeftBackup2()) // 3
        leftRocketSet.add(generateLeftRocketTinyBoi()) // 4

        rightCargoToRocketSet.add(generateRightHabToFrontCargo()) // 0
        rightCargoToRocketSet.add(generateRightCargoBackup()) // 1
        rightCargoToRocketSet.add(generateCargoBackupToStationRight())
        rightCargoToRocketSet.add(generateRightStationToFirstCargo())
        rightCargoToRocketSet.add(generateFirstCargoPlace())

        leftCargoToRocketSet.add(generateLeftHabToFrontCargo()) // 0

        tuning = generateTuning()
    }

    @SuppressWarnings("MagicNumber")
    public object Poses {
        // left rocket auto
        public val leftStartingPosition = Pose2d(Vector2(65, 45), Rotation2d.fromDegrees(0))
        public val leftRocketPosition = Pose2d(Vector2(200.0, 130.0), Rotation2d.fromDegrees(30.0))
        public val leftRocketBackupPosition = Pose2d(Vector2(140, 80), Rotation2d.fromDegrees(90))
        public val leftStationPosition = Pose2d(Vector2(22.0, 137.1), Rotation2d.fromDegrees(180))
        public val leftRocketMidpoint2 = Pose2d(Vector2(200, 103), Rotation2d.fromDegrees(180))
        public val leftRocketMidpoint3 = Pose2d(Vector2(299, 111), Rotation2d.fromDegrees(190))
        public val leftRocketBackPosition = Pose2d(Vector2(253, 134), Rotation2d.fromDegrees(210))

        // right rocket auto
        public val rightStartingPosition = Pose2d(Vector2(65, -45), Rotation2d.fromDegrees(0))
        public val rightRocketPosition = Pose2d(Vector2(200.0, -132.5), Rotation2d.fromDegrees(-30.0))
        public val rightRocketBackupPosition = Pose2d(Vector2(140, -80), Rotation2d.fromDegrees(-90))
        public val rightStationPosition = Pose2d(Vector2(22.0, -136.5), Rotation2d.fromDegrees(180)) // wored at -133
        public val rightRocketMidpoint2 = Pose2d(Vector2(200, -103), Rotation2d.fromDegrees(180))
        public val rightRocketMidpoint3 = Pose2d(Vector2(299, -111), Rotation2d.fromDegrees(190))
        public val rightRocketBackPosition = Pose2d(Vector2(253, -134), Rotation2d.fromDegrees(210))

        // cargoship to rocket left
        public val leftCargoShipToRocketStartingPosition = Pose2d(Vector2(66, 45), Rotation2d.fromDegrees(0.0))
        public val leftCargoShipFront = Pose2d(Vector2(203, 10), Rotation2d.fromDegrees(0.0))
        public val leftCargoMidpoint1 = Pose2d(Vector2(117, 60), Rotation2d.fromDegrees(-80))
        public val leftCargoBackup = Pose2d(Vector2(161, 120), Rotation2d.fromDegrees(180.0))

        // cargosip to rocket right
        public val rightCargoShipToRocketStartingPosition = Pose2d(Vector2(66, -45), Rotation2d.fromDegrees(0.0))
        public val rightCargoShipFront = Pose2d(Vector2(203, -11), Rotation2d.fromDegrees(0.0))
        public val rightCargoMidpoint1 = Pose2d(Vector2(150, -25), Rotation2d.fromDegrees(30.0))
        public val rightCargoBackup = Pose2d(Vector2(140, -120), Rotation2d.fromDegrees(180))

        public val zero = Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0))
        public val tuning = Pose2d(Vector2(25, 15), Rotation2d.fromDegrees(45))
    }

    private fun generatePathToLeftRocket(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftStartingPosition,
            // Poses.leftRocketMidpoint,
            Poses.leftRocketPosition
        )
        return mGenerator.generatePath(false, points, 50.0, 50.0, 5.0, 0.0)
    }

    private fun generateLeftRocketBackup1(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftRocketPosition,
            Poses.leftRocketBackupPosition
        )
        return mGenerator.generatePath(true, points, 50.0, 50.0, 20.0, 0.0) // change back to 50,50,20,0 maybe
    }

    private fun generateLeftBackupToStation(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftRocketBackupPosition,
            Poses.leftStationPosition
        )
        return mGenerator.generatePath(false, points, 50.0, 50.0, 20.0, 0.0) // change back to 50,50,20,0 maybe
    }

    private fun generateLeftBackup2(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftStationPosition,
            Poses.leftRocketMidpoint2,
            Poses.leftRocketMidpoint3
        )
        return mGenerator.generatePath(true, points, 100.0, 100.0, 25.0, 0.0)
    }

    private fun generateLeftRocketTinyBoi(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftRocketMidpoint3,
            Poses.leftRocketBackPosition
        )
        return mGenerator.generatePath(false, points, 50.0, 50.0, 25.0, 0.0)
    }

    private fun generatePathToRightRocket(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightStartingPosition,
            // Poses.rightRocketMidpoint,
            Poses.rightRocketPosition
        )
        return mGenerator.generatePath(false, points, 50.0, 50.0, 5.0, 0.0)
    }

    private fun generateRightRocketBackup1(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightRocketPosition,
            Poses.rightRocketBackupPosition
        )
        return mGenerator.generatePath(true, points, 50.0, 50.0, 20.0, 0.0) // change back to 50,50,20,0 maybe
    }

    private fun generateRightBackupToStation(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightRocketBackupPosition,
            Poses.rightStationPosition
        )
        return mGenerator.generatePath(false, points, 50.0, 50.0, 20.0, 0.0) // change back to 50,50,20,0 maybe
    }

    private fun generateRightBackup2(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightStationPosition,
            Poses.rightRocketMidpoint2,
            Poses.rightRocketMidpoint3
        )
        return mGenerator.generatePath(true, points, 100.0, 100.0, 25.0, 0.0)
    }

    private fun generateRightRocketTinyBoi(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightRocketMidpoint3,
            Poses.rightRocketBackPosition
        )
        return mGenerator.generatePath(false, points, 50.0, 50.0, 25.0, 0.0)
    }

    private fun generateRightHabToFrontCargo(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightCargoShipToRocketStartingPosition,
            Poses.rightCargoShipFront
        )
        return mGenerator.generatePath(false, points, 50.0, 50.0, 10.0, 0.0)
    }

    private fun generateLeftHabToFrontCargo(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftCargoShipToRocketStartingPosition,
            Poses.leftCargoShipFront
        )
        return mGenerator.generatePath(false, points, 58.0, 50.0, 10.0, 0.0) // worked at 55 velo
    }

    private fun generateRightCargoBackup(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightCargoShipFront,
            // Poses.rightCargoShipFront.transformBy(Vector2(-12.0, 0.0))
            Poses.rightCargoMidpoint1,
            Poses.rightCargoBackup
        )
        return mGenerator.generatePath(true, points, 80.0, 50.0, 20.0, 0.0)
    }

    private fun generateCargoBackupToStationRight(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightCargoBackup,
            Poses.rightStationPosition.transformBy(Vector2(-6.5, 4.25))
        )
        return mGenerator.generatePath(false, points, 80.0, 50.0, 15.0, 0.0) // worked at 60 velo
    }

    private fun generateRightStationToFirstCargo(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightStationPosition.transformBy(Vector2(-6.0, 6.0)),
            // Pose2d(Vector2(130.0, -90.0), Rotation2d.fromDegrees(210)),
            Pose2d(Vector2(259.0, -105.0), Rotation2d.fromDegrees(90.0))
        )
        return mGenerator.generatePath(true, points, 70.0, 55.0, 20.0, 0.0)
    }

    private fun generateFirstCargoPlace(): Path {
        val points: Array<Pose2d> = arrayOf(
            Pose2d(Vector2(259.0, -105.0), Rotation2d.fromDegrees(90.0)),
            Pose2d(Vector2(250.0, -50.0), Rotation2d.fromDegrees(90.0))
        )
        return mGenerator.generatePath(false, points, 100.0, 60.0, 20.0, 0.0)
    }

    private fun generateTuning(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.zero,
            Poses.tuning
        )
        return mGenerator.generatePath(false, points, 100.0, 100.0, 5.0, 0.0)
    }
}
