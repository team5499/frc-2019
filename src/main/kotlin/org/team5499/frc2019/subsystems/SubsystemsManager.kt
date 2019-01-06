package org.team5499.frc2019.subsystems

public class SubsystemsManager(drivetrain: Drivetrain, lift: Lift) {

    public val drivetrain: Drivetrain
    public val lift: Lift

    init {
        this.drivetrain = drivetrain
        this.lift = lift
    }

    public fun updateAll() {
        drivetrain.update()
    }

    public fun stopAll() {
        drivetrain.update()
    }

    public fun resetAll() {
        drivetrain.update()
    }
}
