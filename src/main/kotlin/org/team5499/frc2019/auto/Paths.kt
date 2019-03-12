package org.team5499.frc2019.auto

import org.team5499.monkeyLib.path.PathGenerator
import org.team5499.monkeyLib.path.Path
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Vector2

@SuppressWarnings("MagicNumber")
public class Paths(generator: PathGenerator) {

    private val mGenerator: PathGenerator

    public val fromHabToLeftRocket: Path

    public val fromHabToRightRocket: Path
    public val rightRocketBackup: Path
    public val rightBackupToStation: Path
    public val rightRocketBackup2: Path
    public val rightRocketTinyBoi: Path

    public val rightHabToFrontCargo: Path
    // public val rightCargoBackup: Path

    public val tuning: Path

    init {
        mGenerator = generator
        fromHabToLeftRocket = generatePathToLeftRocket()

        fromHabToRightRocket = generatePathToRightRocket()
        rightRocketBackup = generateRightRocketBackup()
        rightBackupToStation = generateRightBackupToStation()
        rightRocketBackup2 = generateRightBackup2()

        rightRocketTinyBoi = generateRightTinyBoi()

        rightHabToFrontCargo = generateRightHabToFrontCargo()
        // rightCargobackup = generateRightCargoBackup()

        tuning = generateTuning()
    }

    @SuppressWarnings("MagicNumber")
    public object Poses {
        // left rocket auto
        public val leftStartingPosition = Pose2d(Vector2(65, 40), Rotation2d.fromDegrees(90.0))
        public val leftRocketMidpoint = Pose2d(Vector2(100, 120), Rotation2d.fromDegrees(25.0))
        public val leftRocketPosition = Pose2d(Vector2(200, 135), Rotation2d.fromDegrees(30.0))

        public val leftRocketBackupPosition = Pose2d(Vector2(140, 80), Rotation2d.fromDegrees(-90))

        public val lefStationPosition = Pose2d(Vector2(31.0, 134.0), Rotation2d.fromDegrees(180))

        // right rocket auto
        public val rightStartingPosition = Pose2d(Vector2(65, -40), Rotation2d.fromDegrees(-90.0))
        public val rightRocketMidpoint = Pose2d(Vector2(100, -125), Rotation2d.fromDegrees(-20.0)) // 100, -120
        public val rightRocketPosition = Pose2d(Vector2(205, -135), Rotation2d.fromDegrees(-30.0)) // 203, -133 worked

        public val rightRocketBackupPosition = Pose2d(Vector2(140, -80), Rotation2d.fromDegrees(-90))

        public val rightStationPosition = Pose2d(Vector2(31.0, -134.0), Rotation2d.fromDegrees(180)) // wored at -133

        public val rightRocketMidpoint2 = Pose2d(Vector2(200, -103), Rotation2d.fromDegrees(180))
        public val rightRocketMidpoint3 = Pose2d(Vector2(299, -111), Rotation2d.fromDegrees(190))

        public val rightRocketBackPosition = Pose2d(Vector2(253, -134), Rotation2d.fromDegrees(210))

        // cargoship to rocket left
        public val leftCargoShipToRocketStartingPosition = Pose2d(Vector2(66, 45), Rotation2d.fromDegrees(0.0))

        // cargosip to rocket right
        public val rightCargoShipToRocketStartingPosition = Pose2d(Vector2(66, -45), Rotation2d.fromDegrees(0.0))
        public val rightCargoShipFront = Pose2d(Vector2(203, -10), Rotation2d.fromDegrees(0.0))

        // public val rightCargoBackup1 = Pose2d(Vector2())

        public val zero = Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0))
        public val tuning = Pose2d(Vector2(25, 15), Rotation2d.fromDegrees(45))
    }

    private fun generatePathToLeftRocket(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftStartingPosition,
            Poses.leftRocketMidpoint,
            Poses.leftRocketPosition
        )
        return mGenerator.generatePath(false, points)
    }

    private fun generatePathToRightRocket(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightStartingPosition,
            Poses.rightRocketMidpoint,
            Poses.rightRocketPosition
        )
        return mGenerator.generatePath(false, points, 50.0, 50.0, 5.0, 0.0)
    }

    private fun generateRightRocketBackup(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightRocketPosition,
            Poses.rightRocketBackupPosition
        )
        return mGenerator.generatePath(true, points, 50.0, 50.0, 20.0, 0.0)
    }

    private fun generateRightBackupToStation(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightRocketBackupPosition,
            Poses.rightStationPosition
        )
        return mGenerator.generatePath(false, points, 50.0, 50.0, 20.0, 0.0)
    }

    private fun generateRightBackup2(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightStationPosition,
            Poses.rightRocketMidpoint2,
            Poses.rightRocketMidpoint3
        )
        return mGenerator.generatePath(true, points, 100.0, 100.0, 25.0, 0.0)
    }

    private fun generateRightTinyBoi(): Path {
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

    private fun generateRightCargoBackup(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.rightCargoShipToRocketStartingPosition,
            Poses.rightCargoShipFront
        )
        return mGenerator.generatePath(true, points, 80.0, 50.0, 20.0, 0.0)
    }

    private fun generateTuning(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.zero,
            Poses.tuning
        )
        return mGenerator.generatePath(false, points, 100.0, 100.0, 5.0, 0.0)
    }
}
