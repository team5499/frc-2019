package org.team5499.frc2019.auto

import org.team5499.monkeyLib.path.PathGenerator
import org.team5499.monkeyLib.path.MirroredPath
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Vector2

@SuppressWarnings("MagicNumber")
public class Paths(generator: PathGenerator) {

    private val mGenerator: PathGenerator

    // rocket auto
    public val fromHabToRocket: MirroredPath
    public val rocketBackup1: MirroredPath
    public val backupToStation: MirroredPath
    public val rocketBackup2: MirroredPath
    public val rocketTinyBoi: MirroredPath

    init {
        mGenerator = generator

        // / rocket auto
        fromHabToRocket = generatePathToRocket()
        rocketBackup1 = generateRocketBackup1()
        backupToStation = generateBackupToStation()
        rocketBackup2 = generateBackup2()
        rocketTinyBoi = generateTinyBoi()
    }

    @SuppressWarnings("MagicNumber")
    public object Poses {
        // rocket
        public val leftStartingPosition = Pose2d(Vector2(65, -40), Rotation2d.fromDegrees(90.0))
        public val rightStartingPosition = Pose2d(Vector2(65, 40), Rotation2d.fromDegrees(-90))

        public val leftRocketMidpoint = Pose2d(Vector2(100, -120), Rotation2d.fromDegrees(-25.0))
        public val leftRocketPosition = Pose2d(Vector2(205, -133), Rotation2d.fromDegrees(-30.0)) // 203, -133 worked

        public val leftRocketBackupPosition = Pose2d(Vector2(140, -80), Rotation2d.fromDegrees(-90))

        public val leftStationPosition = Pose2d(Vector2(32.0, -134.0), Rotation2d.fromDegrees(180)) // wored at -133

        public val leftRocketMidpoint2 = Pose2d(Vector2(200, -99), Rotation2d.fromDegrees(180))
        public val leftRocketMidpoint3 = Pose2d(Vector2(289, -111), Rotation2d.fromDegrees(180))

        public val leftRocketBackPosition = Pose2d(Vector2(253, -134), Rotation2d.fromDegrees(210))

        // cargo ship then rocket
        public val centerStartingPosition = Pose2d(Vector2(65, 0), Rotation2d.fromDegrees(0.0))
        public val leftFrontCargoShip = Pose2d(Vector2(200, 11), Rotation2d.fromDegrees(0.0))
    }

    private fun generatePathToRocket(): MirroredPath {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftStartingPosition,
            Poses.leftRocketMidpoint,
            Poses.leftRocketPosition
        )
        return mGenerator.generateMirroredPath(false, points, 50.0, 50.0, 5.0, 0.0)
    }

    private fun generateRocketBackup1(): MirroredPath {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftRocketPosition,
            Poses.leftRocketBackupPosition
        )
        return mGenerator.generateMirroredPath(true, points, 50.0, 50.0, 20.0, 0.0)
    }

    private fun generateBackupToStation(): MirroredPath {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftRocketBackupPosition,
            Poses.leftStationPosition
        )
        return mGenerator.generateMirroredPath(false, points, 50.0, 50.0, 20.0, 0.0)
    }

    private fun generateBackup2(): MirroredPath {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftStationPosition,
            Poses.leftRocketMidpoint2,
            Poses.leftRocketMidpoint3
        )
        return mGenerator.generateMirroredPath(true, points, 120.0, 100.0, 25.0, 0.0)
    }

    private fun generateTinyBoi(): MirroredPath {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftRocketMidpoint3,
            Poses.leftRocketBackPosition
        )
        return mGenerator.generateMirroredPath(false, points, 50.0, 50.0, 25.0, 0.0)
    }

    private fun generateHabToCenterCargo(): MirroredPath {
        val points: Array<Pose2d> = arrayOf(
            Poses.centerStartingPosition,
            Poses.leftFrontCargoShip
        )
        return mGenerator.generateMirroredPath(false, points, 50.0, 100.0, 10.0, 0.0)
    }
}
