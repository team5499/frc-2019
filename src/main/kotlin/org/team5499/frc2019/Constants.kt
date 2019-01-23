package org.team5499.frc2019

import org.team5499.dashboard.Dashboard

@SuppressWarnings("MagicNumber")
public object Constants {

    @Suppress("LongMethod")
    public fun pushValuesToDashboard() {
        Dashboard.setVariable("VEL_KP", PID.VEL_KP)
        Dashboard.setVariable("VEL_KI", PID.VEL_KI)
        Dashboard.setVariable("VEL_KD", PID.VEL_KD)
        Dashboard.setVariable("VEL_KF", PID.VEL_KF)
        Dashboard.setVariable("VEL_IZONE", PID.VEL_IZONE)
        Dashboard.setVariable("VEL_MAX_OUTPUT", PID.VEL_MAX_OUTPUT)

        Dashboard.setVariable("POS_KP", PID.POS_KP)
        Dashboard.setVariable("POS_KI", PID.POS_KI)
        Dashboard.setVariable("POS_KD", PID.POS_KD)
        Dashboard.setVariable("POS_KF", PID.POS_KF)
        Dashboard.setVariable("POS_IZONE", PID.POS_IZONE)
        Dashboard.setVariable("POS_MAX_OUTPUT", PID.POS_MAX_OUTPUT)

        Dashboard.setVariable("ANGLE_KP", PID.ANGLE_KP)
        Dashboard.setVariable("ANGLE_KI", PID.ANGLE_KI)
        Dashboard.setVariable("ANGLE_KD", PID.ANGLE_KD)
        Dashboard.setVariable("ANGLE_KF", PID.ANGLE_KF)
        Dashboard.setVariable("ANGLE_IZONE", PID.ANGLE_IZONE)
        Dashboard.setVariable("ANGLE_MAX_OUTPUT", PID.ANGLE_MAX_OUTPUT)

        Dashboard.setVariable("TURN_KP", PID.TURN_KP)
        Dashboard.setVariable("TURN_KI", PID.TURN_KI)
        Dashboard.setVariable("TURN_KD", PID.TURN_KD)
        Dashboard.setVariable("TURN_KF", PID.TURN_KF)
        Dashboard.setVariable("TURN_IZONE", PID.TURN_IZONE)
        Dashboard.setVariable("TURN_MAX_OUTPUT", PID.TURN_MAX_OUTPUT)

        Dashboard.setVariable("FIXED_KP", PID.FIXED_KP)
        Dashboard.setVariable("FIXED_KI", PID.FIXED_KI)
        Dashboard.setVariable("FIXED_KD", PID.FIXED_KD)
        Dashboard.setVariable("FIXED_KF", PID.FIXED_KF)
        Dashboard.setVariable("FIXED_IZONE", PID.FIXED_IZONE)
        Dashboard.setVariable("FIXED_MAX_OUTPUT", PID.FIXED_MAX_OUTPUT)

        Dashboard.setVariable("INVERT_FIXED_AUX_PIDF", PID.INVERT_FIXED_AUX_PIDF)
        Dashboard.setVariable("INVERT_ANGLE_AUX_PIDF", PID.INVERT_ANGLE_AUX_PIDF)
        Dashboard.setVariable("INVERT_TURN_AUX_PIDF", PID.INVERT_TURN_AUX_PIDF)
    }

    @Suppress("LongMethod")
    public fun pullValuesFromDashboard() {
        PID.VEL_KP = Dashboard.getVariable<Double>("VEL_KP")
        PID.VEL_KI = Dashboard.getVariable<Double>("VEL_KI")
        PID.VEL_KD = Dashboard.getVariable<Double>("VEL_KD")
        PID.VEL_KF = Dashboard.getVariable<Double>("VEL_KF")
        PID.VEL_IZONE = Dashboard.getVariable<Int>("VEL_IZONE")
        PID.VEL_MAX_OUTPUT = Dashboard.getVariable<Double>("VEL_MAX_OUTPUT")

        PID.POS_KP = Dashboard.getVariable<Double>("POS_KP")
        PID.POS_KI = Dashboard.getVariable<Double>("POS_KI")
        PID.POS_KD = Dashboard.getVariable<Double>("POS_KD")
        PID.POS_KF = Dashboard.getVariable<Double>("POS_KF")
        PID.POS_IZONE = Dashboard.getVariable<Int>("POS_IZONE")
        PID.POS_MAX_OUTPUT = Dashboard.getVariable<Double>("POS_MAX_OUTPUT")

        PID.ANGLE_KP = Dashboard.getVariable<Double>("ANGLE_KP")
        PID.ANGLE_KI = Dashboard.getVariable<Double>("ANGLE_KI")
        PID.ANGLE_KD = Dashboard.getVariable<Double>("ANGLE_KD")
        PID.ANGLE_KF = Dashboard.getVariable<Double>("ANGLE_KF")
        PID.ANGLE_IZONE = Dashboard.getVariable<Int>("ANGLE_IZONE")
        PID.ANGLE_MAX_OUTPUT = Dashboard.getVariable<Double>("ANGLE_MAX_OUTPUT")

        PID.TURN_KP = Dashboard.getVariable<Double>("TURN_KP")
        PID.TURN_KI = Dashboard.getVariable<Double>("TURN_KI")
        PID.TURN_KD = Dashboard.getVariable<Double>("TURN_KD")
        PID.TURN_KF = Dashboard.getVariable<Double>("TURN_KF")
        PID.TURN_IZONE = Dashboard.getVariable<Int>("TURN_IZONE")
        PID.TURN_MAX_OUTPUT = Dashboard.getVariable<Double>("TURN_MAX_OUTPUT")

        PID.FIXED_KP = Dashboard.getVariable<Double>("FIXED_KP")
        PID.FIXED_KI = Dashboard.getVariable<Double>("FIXED_KI")
        PID.FIXED_KD = Dashboard.getVariable<Double>("FIXED_KD")
        PID.FIXED_KF = Dashboard.getVariable<Double>("FIXED_KF")
        PID.FIXED_IZONE = Dashboard.getVariable<Int>("FIXED_IZONE")
        PID.FIXED_MAX_OUTPUT = Dashboard.getVariable<Double>("FIXED_MAX_OUTPUT")

        PID.INVERT_ANGLE_AUX_PIDF = Dashboard.getVariable<Boolean>("INVERT_ANGLE_AUX_PIDF")
        PID.INVERT_FIXED_AUX_PIDF = Dashboard.getVariable<Boolean>("INVERT_FIXED_AUX_PIDF")
        PID.INVERT_TURN_AUX_PIDF = Dashboard.getVariable<Boolean>("INVERT_TURN_AUX_PIDF")
    }

    public const val DASHBOARD_PORT = 5800
    public const val ROBOT_UPDATE_PERIOD = 0.005
    public const val TALON_UPDATE_PERIOD_MS = 1
    public const val TALON_PIDF_UPDATE_PERIOD_MS = 1

    object Input {
        public const val DRIVER_PORT = 0
        public const val CODRIVER_PORT = 1

        public const val JOYSTICK_DEADBAND = 0.02
        public const val TURN_MULT = 0.4
    }

    object PID {
        public var MAX_LIFT_VELOCITY_SETPOINT = 1000 // encoder ticks per 100ms

        public var MAX_VELOCITY_SETPOINT = 100.0 // inches per seconds

        public var ACCEPTABLE_VELOCITY_THRESHOLD = 3.0 // inches / s
        public var ACCEPTABLE_TURN_ERROR = 3.0 // degrees (?)
        public var ACCEPTABLE_DISTANCE_ERROR = 2.0 // inches

        public var ACCEPTABLE_VELOCITY_THRESHOLD_LIFT = 3.0 // inches / s
        public var ACCEPTABLE_DISTANCE_ERROR_LIFT = 1.0 // inches

        public var ACCEPTALE_VELOCITY_THRESHOLD_WRIST = 3.0 // inches / s
        public var ACCEPTABLE_DISTANCE_ERROR_WRIST = 1.0 // inches

        public var VEL_KP = 2.5
        public var VEL_KI = 0.0
        public var VEL_KD = 0.0
        public var VEL_KF = 0.95
        public var VEL_IZONE = 10
        public var VEL_MAX_OUTPUT = 1.0

        public var POS_KP = 0.79
        public var POS_KI = 0.0
        public var POS_KD = 0.3
        public var POS_KF = 0.0
        public var POS_IZONE = 10
        public var POS_MAX_OUTPUT = 0.5

        public var ANGLE_KP = 2.0
        public var ANGLE_KI = 0.0
        public var ANGLE_KD = 0.0
        public var ANGLE_KF = 0.0
        public var ANGLE_IZONE = 10
        public var ANGLE_MAX_OUTPUT = 1.0

        public var TURN_KP = 1.3
        public var TURN_KI = 0.0
        public var TURN_KD = 12.0
        public var TURN_KF = 0.0
        public var TURN_IZONE = 10
        public var TURN_MAX_OUTPUT = 1.0

        public var FIXED_KP = 0.1
        public var FIXED_KI = 0.0
        public var FIXED_KD = 0.0
        public var FIXED_KF = 0.0
        public var FIXED_IZONE = 10
        public var FIXED_MAX_OUTPUT = 0.5

        public var INVERT_FIXED_AUX_PIDF = true
        public var INVERT_ANGLE_AUX_PIDF = true
        public var INVERT_TURN_AUX_PIDF = false
    }

    object HardwarePorts {
        public const val LEFT_DRIVE_MASTER = 6
        public const val LEFT_DRIVE_SLAVE1 = 1
        public const val LEFT_DRIVE_SLAVE2 = 22 // check this later

        public const val RIGHT_DRIVE_MASTER = 8
        public const val RIGHT_DRIVE_SLAVE1 = 2
        public const val RIGHT_DRIVE_SLAVE2 = 23

        public const val LIFT_MASTER = 5
        public const val LIFT_SLAVE = 23

        public const val LIFT_ZERO_SENSOR = 1
        public const val WRIST_MASTER = 4 // check this later

        public const val INTAKE_MASTER = 27

        public const val GYRO_PORT = 10
    }

    object Units {
        public const val ENCODER_TICKS_PER_ROTATION = 4096
        public const val TURN_UNITS_PER_ROTATION = 3600 // for gyro
        public const val PIGEON_UNITS_PER_ROTATION = 8192
    }

    object Dimensions {
        public const val WHEEL_BASE = 20.0 // inches
        public const val WHEEL_DIAMETER = 6.0 // inches
        public const val WHEEL_RADIUS = WHEEL_DIAMETER / 2.0
        public const val WHEEL_CIR = WHEEL_DIAMETER * Math.PI

        public const val SPROCKET_DIAMETER_LIFT = 1.23 // inches
        public const val SPROCKET_RADIUS_LIFT = SPROCKET_DIAMETER_LIFT / 2.0
        public const val SPROCKET_CIR_LIFT = SPROCKET_DIAMETER_LIFT * Math.PI
    }

    object Auto {
        public const val LOOKAHEAD_DISTANCE: Double = 12.0
    }
}
