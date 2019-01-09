package org.team5499.frc2019;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;

import org.team5499.frc2019.Robot;

public final class Main {

    private Main() {}

    public static void main() {
        RobotBase.startRobot(Robot::new);
    }

}
