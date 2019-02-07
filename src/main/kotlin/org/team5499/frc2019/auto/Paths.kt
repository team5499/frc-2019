package org.team5499.frc2019.auto

import org.team5499.monkeyLib.path.PathGenerator
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Vector2

public class Paths(generator: PathGenerator) {

    private val mGenerator: PathGenerator

    init {
        mGenerator = generator
    }

    @SuppressWarnings("MagicNumber")
    private object Poses {
        public val leftStartingPosition = Pose2d(Vector2(60, 36), Rotation2d.fromDegrees(90.0))
        // public val centerStartingPosition =
    }
}
