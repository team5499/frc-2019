package org.team5499.frc2019.input

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.DriverStation

public class WheelDriver(wheel: Joystick, throttle: Joystick) : IDriverControls {

    private val mWheel: Joystick
    private val mThrottle: Joystick

    init {
        mWheel = wheel
        mThrottle = throttle
    }

    public override fun getThrottle() = mThrottle.getY()

    public override fun getTurn() = mWheel.getX()

    public override fun getLeft(): Double {
        DriverStation.reportWarning("\"getLeft()\" attribute from WheelDriver does not exist!", false)
        return 0.0
    }

    public override fun getRight(): Double {
        DriverStation.reportWarning("\"getRight()\" attribute from WheelDriver does not exist!", false)
        return 0.0
    }

    public override fun getQuickTurn() = mWheel.getRawButton(1) // paddle

    public override fun getCreep() = mThrottle.getTrigger()

    public override fun getExitAuto() = mWheel.getRawButton(1) // paddle

    public override fun getStow() = false // change this later

    public override fun getAutoAlign() = false
}
