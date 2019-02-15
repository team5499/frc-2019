package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem

public class SubsystemsManager(
    drivetrain: Drivetrain,
    lift: Lift,
    intake: Intake,
    hatchMech: HatchMech,
    vision: Vision
) {

    public val drivetrain: Drivetrain
    public val lift: Lift
    public val intake: Intake
    public val vision: Vision
    public val hatchMech: HatchMech

    private val mList: List<Subsystem>

    init {
        this.drivetrain = drivetrain
        this.lift = lift
        this.intake = intake
        this.vision = vision
        this.hatchMech = hatchMech
        mList = listOf(this.drivetrain, this.lift, this.intake, this.vision, this.hatchMech)
    }

    public fun updateAll() {
        for (s in mList) {
            s.update()
        }
    }

    public fun stopAll() {
        for (s in mList) {
            s.stop()
        }
    }

    public fun resetAll() {
        for (s in mList) {
            s.reset()
        }
    }
}
