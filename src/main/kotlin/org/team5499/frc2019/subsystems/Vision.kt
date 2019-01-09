package org.team5499.frc2019.subsystems

import org.team5499.monkeyLib.Subsystem

import edu.wpi.first.networktables.NetworkTableInstance

@SuppressWarnings("MagicNumber")
public class Vision : Subsystem() {

    public enum class Pipeline {
        NONE,
        DRIVER,
        ROCKET,
        TEST
    }

    public fun changePipeline(pipe: Pipeline) {
        val table = NetworkTableInstance.getDefault().getTable("limelight")
        when (pipe) {
            Pipeline.NONE -> {
                table.getEntry("ledMode").setNumber(1)
                table.getEntry("camMode").setNumber(0)
            }
            Pipeline.DRIVER -> {
                table.getEntry("ledMode").setNumber(1)
                table.getEntry("camMode").setNumber(1)
            }
            Pipeline.ROCKET -> {
                table.getEntry("ledMode").setNumber(3)
                table.getEntry("camMode").setNumber(0)
            }
            Pipeline.TEST -> {
                table.getEntry("ledMode").setNumber(2)
                table.getEntry("camMode").setNumber(0)
            }
        }
    }

    public override fun update() {
        val table = NetworkTableInstance.getDefault().getTable("limelight")
        val tx = table.getEntry("tx")
        val ty = table.getEntry("ty")
        val ta = table.getEntry("ta")
    }

    public override fun stop() {
    }

    public override fun reset() {
    }
}
