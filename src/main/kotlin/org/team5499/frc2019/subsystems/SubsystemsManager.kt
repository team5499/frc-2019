package org.team5499.frc2019.subsystems

public class SubsystemsManager(drivetrain: Drivetrain) {

    public val drivetrain: Drivetrain

    init {
        this.drivetrain = drivetrain
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