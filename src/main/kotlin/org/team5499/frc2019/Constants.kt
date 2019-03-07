package org.team5499.frc2019

import org.team5499.dashboard.DashboardVar

@SuppressWarnings("MagicNumber")
public object Constants {

    fun initConstants() {
        DashboardVar.initClassProps(Constants::class)
    }

    public const val ROBOT_UPDATE_PERIOD = 0.02 // maybe change back to 0.005
    public const val TALON_UPDATE_PERIOD_MS = 10
    public const val TALON_PIDF_UPDATE_PERIOD_MS = 1

    public const val EPSILON = 1E-5

    object Input {
        // ports
        public const val DRIVER_PORT = 0
        public const val CODRIVER_BUTTON_BOARD_PORT = 1
        public const val CODRIVER_JOYSTICK_PORT = 2

        // driver constants
        public const val JOYSTICK_DEADBAND = 0.07
        public const val TURN_MULT = 0.4
        public const val SLOW_MULT = 0.5
        public const val DRIVER_STOW_TIMEOUT = 2.0 // seconds

        // codriver constants
        public const val MANUAL_CONTROL_DEADBAND = 0.07
    }

    object Drivetrain {
        // talon port
        public const val LEFT_MASTER_TALON_PORT = 6
        public const val LEFT_SLAVE1_TALON_PORT = 7
        public const val LEFT_SLAVE2_TALON_PORT = 8

        public const val RIGHT_MASTER_TALON_PORT = 12
        public const val RIGHT_SLAVE1_TALON_PORT = 2
        public const val RIGHT_SLAVE2_TALON_PORT = 3

        // gyro
        public const val GYRO_PORT = 13

        // units
        public const val ENCODER_TICKS_PER_ROTATION = 4096
        public const val TURN_UNITS_PER_ROTATION = 3600 // for gyro
        public const val PIGEON_UNITS_PER_ROTATION = 8192

        // dimensions
        public const val WHEEL_BASE = 27.0 // inches
        public const val WHEEL_DIAMETER = 6.0 // inches
        public const val WHEEL_RADIUS = WHEEL_DIAMETER / 2.0
        public const val WHEEL_CIR = WHEEL_DIAMETER * Math.PI

        // pid thresholds
        public const val MAX_VELOCITY = 100.0 // inches per second
        public const val MAX_ACCELERATION = 50.0 // inches per second^2
        public const val ACCEPTABLE_VELOCITY_THRESHOLD = 3.0 // inches / s
        public const val ACCEPTABLE_TURN_ERROR = 3.0 // degrees (?)
        public const val ACCEPTABLE_DISTANCE_ERROR = 2.0 // inches

        // pid constants
        public const val VEL_KP = 1.3
        public const val VEL_KI = 0.0
        public const val VEL_KD = 0.0
        public const val VEL_KF = 0.60
        public const val VEL_IZONE = 10
        public const val VEL_MAX_OUTPUT = 1.0

        public const val POS_KP = 0.8
        public const val POS_KI = 0.0
        public const val POS_KD = 0.2
        public const val POS_KF = 0.0
        public const val POS_IZONE = 10
        public const val POS_MAX_OUTPUT = 0.5

        public const val ANGLE_KP = 2.0
        public const val ANGLE_KI = 0.0
        public const val ANGLE_KD = 0.0
        public const val ANGLE_KF = 0.0
        public const val ANGLE_IZONE = 10
        public const val ANGLE_MAX_OUTPUT = 1.0

        public const val TURN_KP = 1.5
        public const val TURN_KI = 0.0
        public const val TURN_KD = 0.0
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
        // ports
        public const val MASTER_TALON_PORT = 4
        public const val SLAVE_TALON_PORT = 5

        // pid
        public var KP by DashboardVar(0.7) // worked a 0.09
        public var KI by DashboardVar(0.0)
        public var KD by DashboardVar(0.0)
        public var KF by DashboardVar(0.0)

        public const val MOTION_MAGIC_VELOCITY = 11000 // 10500 before
        public const val MOTION_MAGIC_ACCELERATION = 11000 // 9000 before

        // heights (carriage height in inches)
        public val ROCKET_HOLE_SPACING by DashboardVar(27.0)
        public var STOW_HEIGHT by DashboardVar(3.5)

        public var HATCH_LOW_HEIGHT by DashboardVar(8.5)
        public val HATCH_MID_HEIGHT by DashboardVar(34.0)
        public val HATCH_HIGH_HEIGHT by DashboardVar(59.0)

        public var BALL_LOW_HEIGHT by DashboardVar(4.5) // 4.5
        public val BALL_MID_HEIGHT by DashboardVar(32.0) // 32
        public val BALL_HIGH_HEIGHT by DashboardVar(58.0) // 58
        public var BALL_HUMAN_PLAYER_HEIGHT by DashboardVar(20.0)

        // constants
        public const val ENCODER_REDUCTION = 38.0 / 24.0 // reduction from encoder shaft to output shaft
        public const val ENCODER_TICKS_PER_ROTATION = 4096 // of the encoder shaft
        public const val MAX_ENCODER_TICKS = (8400 * 4 * ENCODER_REDUCTION).toInt() // CHANGE THIS, worked on 8100
        public const val MIN_ENCODER_TICKS = (400 * ENCODER_REDUCTION).toInt() // CHANGE THIS
        public const val ZEROING_THRESHOLD = 5 // ticks per 100/ms
        public const val ZEROING_SPEED = -0.2 // percent output
        public const val ZEROING_TIMEOUT = 0.5 // seconds

        public const val MAX_VELOCITY_SETPOINT = 10000 // ticks per 100ms

        public const val ACCEPTABLE_VELOCITY_THRESHOLD = 3.0 // inches / s
        public const val ACCEPTABLE_DISTANCE_ERROR = 1.0 // inche

        // dimension
        public const val SPROCKET_DIAMETER = 1.23 // inches
        public const val SPROCKET_RADIUS = SPROCKET_DIAMETER / 2.0
        public const val SPROCKET_CIR = SPROCKET_DIAMETER * Math.PI
    }

    object Intake {
        public const val TALON_PORT = 9

        public const val INTAKE_SPEED = 1.0
        public const val OUTTAKE_SPEED = -1.0 // -.45
        public const val IDLE_SPEED = 0.0
        public const val HOLD_SPEED = 0.2
    }

    object Hatch {
        public const val TALON_PORT = 10

        // public const val HATCH_KP = 2.0
        // public const val HATCH_KI = 0.0
        // public const val HATCH_KD = 1.0

        public var HATCH_KP by DashboardVar(3.5)
        public var HATCH_KI by DashboardVar(0.0)
        public var HATCH_KD by DashboardVar(0.5)

        // positions, all in pot ticks
        public var POSITION_OFFSET by DashboardVar(10)

        public var TOP_STOW_POSITION by DashboardVar(0)
        public var BOTTOM_STOW_POSITION by DashboardVar(770)
        public var DEPLOY_POSITION by DashboardVar(360)
        public var HOLD_POSITION by DashboardVar(160)
    }

    object Auto {
        public const val LOOKAHEAD_DISTANCE = 18.0
    }
}
