package org.team5499.frc2019.subsystems

public class SubsystemsManager(drivetrain: Drivetrain, lift: Lift, intake: Intake, vision: Vision) {

    public val drivetrain: Drivetrain
    public val lift: Lift
    public val intake: Intake
    public val vision: Vision

    init {
        this.drivetrain = drivetrain
        this.lift = lift
        this.intake = intake
        this.vision = vision
    }

    public fun updateAll() {
        drivetrain.update()
        lift.update()
        intake.update()
        vision.update()
    }

    public fun stopAll() {
        drivetrain.stop()
        lift.stop()
        intake.stop()
        vision.stop()
    }

    public fun resetAll() {
        drivetrain.reset()
        lift.reset()
        intake.reset()
        vision.reset()
    }
}
