package org.team5499.frc2019

@SuppressWarnings("MagicNumber")
public object Constants {

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
        public const val MAX_LIFT_VELOCITY_SETPOINT = 1000 // encoder ticks per 100ms

        public const val MAX_VELOCITY_SETPOINT = 100.0 // inches per seconds

        public const val ACCEPTABLE_VELOCITY_THRESHOLD = 3.0 // inches / s
        public const val ACCEPTABLE_TURN_ERROR = 3.0 // degrees (?)
        public const val ACCEPTABLE_DISTANCE_ERROR = 2.0 // inches

        public const val ACCEPTABLE_VELOCITY_THRESHOLD_LIFT = 3.0 // inches / s
        public const val ACCEPTABLE_DISTANCE_ERROR_LIFT = 1.0 // inches

        public const val ACCEPTALE_VELOCITY_THRESHOLD_WRIST = 3.0 // inches / s
        public const val ACCEPTABLE_DISTANCE_ERROR_WRIST = 1.0 // inches

        public const val VEL_KP = 2.5
        public const val VEL_KI = 0.0
        public const val VEL_KD = 0.0
        public const val VEL_KF = 0.95
        public const val VEL_IZONE = 10
        public const val VEL_MAX_OUTPUT = 1.0

        public const val POS_KP = 0.79
        public const val POS_KI = 0.0
        public const val POS_KD = 0.3
        public const val POS_KF = 0.0
        public const val POS_IZONE = 10
        public const val POS_MAX_OUTPUT = 0.5

        public const val ANGLE_KP = 2.0
        public const val ANGLE_KI = 0.0
        public const val ANGLE_KD = 0.0
        public const val ANGLE_KF = 0.0
        public const val ANGLE_IZONE = 10
        public const val ANGLE_MAX_OUTPUT = 1.0

        public const val TURN_KP = 1.3
        public const val TURN_KI = 0.0
        public const val TURN_KD = 12.0
        public const val TURN_KF = 0.0
        public const val TURN_IZONE = 10
        public const val TURN_MAX_OUTPUT = 1.0

        public const val FIXED_KP = 0.0
        public const val FIXED_KI = 0.0
        public const val FIXED_KD = 0.0
        public const val FIXED_KF = 0.0
        public const val FIXED_IZONE = 10
        public const val FIXED_MAX_OUTPUT = 0.5

        public const val INVERT_FIXED_AUX_PIDF = true
        public const val INVERT_ANGLE_AUX_PIDF = true
        public const val INVERT_TURN_AUX_PIDF = false
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

    object Hatch {
        // If true, hatch will move in the negitive direction when opening
        public const val OPEN_NEGIGIVE: Boolean = false
        // If below this number, the hatch reads as closed. If above, the hatch reads as open (or vice versa)
        public const val HATCH_OPEN_DETECTION_LIMIT = 30
        // If true, the hatch will read as open if the number is below HATCH_OPEN_DETECTION_LIMIT
        public const val OPEN_DETECTOR_NEGITIVE: Boolean = false
    }
}
