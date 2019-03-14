package org.team5499.frc2019.auto.actions

import org.team5499.frc2019.subsystems.Lift
import org.team5499.frc2019.subsystems.Lift.LiftHeight
import org.team5499.monkeyLib.auto.Action

import org.tinylog.Logger

/**
 * An action that sets the elevator to a certain height.
 *
 * @param timeoutseconds The number of seconds to wait before canceling the command
 * @param height The height to go to
 * @param lift The lift to act on
 */
public class LiftAction(
    val inches: Double,
    val lift: Lift
) : Action(0.0) {

    public constructor(height: LiftHeight, lift: Lift): this(height.carriageHeightInches(), lift)

    public override fun start() {
        Logger.tag("ACTIONS").info("Starting lift action!" as Any)
        lift.setCarriagePosition(inches)
    }

    public override fun next() = true
}
