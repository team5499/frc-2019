package org.team5499.frc2019.auto

import org.team5499.monkeyLib.path.PathGenerator
import org.team5499.monkeyLib.path.Path
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Vector2

public class Paths(generator: PathGenerator) {

    private val mGenerator: PathGenerator

    public val toLeftRocket: Path

    init {
        mGenerator = generator
        toLeftRocket = generatePathToLeftRocket()
    }

    @SuppressWarnings("MagicNumber")
    public object Poses {
        public val leftStartingPosition = Pose2d(Vector2(65, 40), Rotation2d.fromDegrees(90.0))
        public val leftRocketMidpoint = Pose2d(Vector2(100, 120), Rotation2d.fromDegrees(25.0))
        public val leftRocketPosition = Pose2d(Vector2(200, 133), Rotation2d.fromDegrees(30.0))
    }

    public fun generatePathToLeftRocket(): Path {
        val points: Array<Pose2d> = arrayOf(
            Poses.leftStartingPosition,
            Poses.leftRocketMidpoint,
            Poses.leftRocketPosition
        )
        return mGenerator.generatePath(false, points)
    }
}
