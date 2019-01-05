package org.team5499.frc2019.subsystems

import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.VictorSPX

public class Drivetrain(): Subsystem() {

    private val mLeftMaster: TalonSRX
    private val mLeftSlave1: VictorSPX
    private val mLeftSlave2: VictorSPX

    private val mRightMaster: TalonSRX
    private val mRightSlave1: VictorSPX
    private val mRightSlave2: VictorSPX

    private val mGryo: PigeonIMU

    init {
        // mLeftMaster = 
    }

    public fun update() {

    }

}