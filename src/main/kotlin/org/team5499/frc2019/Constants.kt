package org.team5499.frc2019

@SuppressWarnings("MagicNumber")
public object Constants {

    public const val ROBOT_UPDATE_PERIOD = 0.005
    public const val TALON_UPDATE_PERIOD_MS = 1
    public const val TALON_PIDF_UPDATE_PERIOD_MS = 1

    object Input {
        public const val DRIVER_PORT = 0
        public const val CODRIVER_BUTTON_BOARD_PORT = 1
        public const val CODRIVER_JOYSTICK_PORT = 2

        public const val JOYSTICK_DEADBAND = 0.02
        public const val TURN_MULT = 0.4
    }

    object Drivetrain {
        // talon port
        public const val LEFT_MASTER_TALON_PORT = 6
        public const val LEFT_SLAVE1_TALON_PORT = 1
        public const val LEFT_SLAVE2_TALON_PORT = 22 // check this later

        public const val RIGHT_MASTER_TALON_PORT = 8
        public const val RIGHT_SLAVE1_TALON_PORT = 2
        public const val RIGHT_SLAVE2_TALON_PORT = 23

        // gyro
        public const val GYRO_PORT = 10

        // units
        public const val ENCODER_TICKS_PER_ROTATION = 4096
        public const val TURN_UNITS_PER_ROTATION = 3600 // for gyro
        public const val PIGEON_UNITS_PER_ROTATION = 8192

        // dimensions
        public const val WHEEL_BASE = 20.0 // inches
        public const val WHEEL_DIAMETER = 6.0 // inches
        public const val WHEEL_RADIUS = WHEEL_DIAMETER / 2.0
        public const val WHEEL_CIR = WHEEL_DIAMETER * Math.PI

        // pid thresholds
        public const val MAX_VELOCITY_SETPOINT = 100.0 // inches per seconds
        public const val ACCEPTABLE_VELOCITY_THRESHOLD = 3.0 // inches / s
        public const val ACCEPTABLE_TURN_ERROR = 3.0 // degrees (?)
        public const val ACCEPTABLE_DISTANCE_ERROR = 2.0 // inches

        // pid constants
        public const val VEL_KP = 2.5
        public const val VEL_KI = 0.0
        public const val VEL_KD = 0.0
        public const val VEL_KF = 0.95
        public const val VEL_IZONE = 10
        public const val VEL_MAX_OUTPUT = 1.0

        public const val POS_KP = 1.0
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

    object Lift {
        public const val MASTER_TALON_PORT = 5
        public const val SLAVE_TALON_PORT = 23

        public const val MAX_VELOCITY_SETPOINT = 1000 // ticks per 100ms

        public const val ACCEPTABLE_VELOCITY_THRESHOLD = 3.0 // inches / s
        public const val ACCEPTABLE_DISTANCE_ERROR = 1.0 // inche

        public const val SPROCKET_DIAMETER = 1.23 // inches
        public const val SPROCKET_RADIUS = SPROCKET_DIAMETER / 2.0
        public const val SPROCKET_CIR = SPROCKET_DIAMETER * Math.PI
    }

    object Intake {
        public const val TALON_PORT = 27

        public const val INTAKE_SPEED = 0.6
        public const val OUTTAKE_SPEED = -0.6
        public const val IDLE_SPEED = 0.0
        public const val HOLD_SPEED = 0.1
    }

    object Hatch

    object Auto {
        public const val LOOKAHEAD_DISTANCE: Double = 12.0
    }
}
