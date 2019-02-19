package org.team5499.frc2019.auto

import org.team5499.monkeyLib.path.PathGenerator
import org.team5499.monkeyLib.path.Path
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Vector2

public class Paths(generator: PathGenerator) {

    private val mGenerator: PathGenerator

    public val testPath: Path

    init {
        mGenerator = generator
        testPath = generateTestPath()
    }

    @SuppressWarnings("MagicNumber")
    private object Poses {
        public val leftStartingPosition = Pose2d(Vector2(0, 0), Rotation2d.fromDegrees(0.0))
        public val testPoint = Pose2d(Vector2(120, 0), Rotation2d.fromDegrees(-45.0))
        public val testPoint2 = Pose2d(Vector2(120, -120), Rotation2d.fromDegrees(-90.0))
    }

    public fun generateTestPath(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftStartingPosition,
            Poses.testPoint,
            Poses.testPoint2
        )
        return mGenerator.generatePath(false, points)
    }
}
