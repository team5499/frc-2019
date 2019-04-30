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
        public var MAX_VELOCITY by DashboardVar(100.0) // inches per second
        public var MAX_ACCELERATION by DashboardVar(50.0) // inches per second^2
        public var ACCEPTABLE_VELOCITY_THRESHOLD by DashboardVar(3.0) // inches / s
        public var ACCEPTABLE_TURN_ERROR by DashboardVar(3.0) // degrees (?)
        public var ACCEPTABLE_DISTANCE_ERROR by DashboardVar(2.0) // inches

        // pid constants
        public var VEL_KP by DashboardVar(1.3)
        public var VEL_KI by DashboardVar(0.0)
        public var VEL_KD by DashboardVar(0.6)
        public var VEL_KF by DashboardVar(0.6)
        public var VEL_IZONE by DashboardVar(10)
        public var VEL_MAX_OUTPUT by DashboardVar(1.0)

        public var POS_KP by DashboardVar(0.8)
        public var POS_KI by DashboardVar(0.0)
        public var POS_KD by DashboardVar(0.2)
        public var POS_KF by DashboardVar(0.0)
        public var POS_IZONE by DashboardVar(10)
        public var POS_MAX_OUTPUT by DashboardVar(0.5)

        public var ANGLE_KP by DashboardVar(2.0)
        public var ANGLE_KI by DashboardVar(0.0)
        public var ANGLE_KD by DashboardVar(0.0)
        public var ANGLE_KF by DashboardVar(0.0)
        public var ANGLE_IZONE by DashboardVar(10)
        public var ANGLE_MAX_OUTPUT by DashboardVar(1.0)

        public var TURN_KP by DashboardVar(0.1)
        public var TURN_KI by DashboardVar(0.0)
        public var TURN_KD by DashboardVar(0.0)
        public var TURN_KF by DashboardVar(0.0)
        public var TURN_IZONE by DashboardVar(10)
        public var TURN_MAX_OUTPUT by DashboardVar(1.0)

        public var FIXED_KP by DashboardVar(0.0)
        public var FIXED_KI by DashboardVar(0.0)
        public var FIXED_KD by DashboardVar(0.0)
        public var FIXED_KF by DashboardVar(0.0)
        public var FIXED_IZONE by DashboardVar(10)
        public var FIXED_MAX_OUTPUT by DashboardVar(0.5)

        public var INVERT_FIXED_AUX_PIDF by DashboardVar(true)
        public var INVERT_ANGLE_AUX_PIDF by DashboardVar(true)
        public var INVERT_TURN_AUX_PIDF by DashboardVar(false)
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

        public var HATCH_LOW_HEIGHT by DashboardVar(7.0)
        public val HATCH_MID_HEIGHT by DashboardVar(34.5)
        public val HATCH_HIGH_HEIGHT by DashboardVar(60.5)

        public var BALL_LOW_HEIGHT by DashboardVar(3.5) // 4.5
        public val BALL_MID_HEIGHT by DashboardVar(30.0) // 32
        public val BALL_HIGH_HEIGHT by DashboardVar(57.0) // 58
        public var BALL_HUMAN_PLAYER_HEIGHT by DashboardVar(20.0)

        // constants
        public const val ENCODER_REDUCTION = 38.0 / 24.0 // reduction from encoder shaft to output shaft
        public const val ENCODER_TICKS_PER_ROTATION = 4096 // of the encoder shaft
        public const val MAX_ENCODER_TICKS = (8400 * 4 * ENCODER_REDUCTION).toInt() // CHANGE THIS, worked on 8100
        public const val MIN_ENCODER_TICKS = (400 * ENCODER_REDUCTION).toInt() // CHANGE THIS
        public const val ZEROING_THRESHOLD = 5 // ticks per 100/ms
        public const val ZEROING_SPEED = -0.2 // percent output
        public const val ZEROING_TIMEOUT = 0.75 // seconds

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
        public const val OUTTAKE_SPEED = -0.85 // -.45
        public const val IDLE_SPEED = 0.0
        public const val HOLD_SPEED = 0.2
    }

    object Hatch {
        public const val TALON_PORT = 10

        public var KP by DashboardVar(3.5)
        public var KI by DashboardVar(0.0)
        public var KD by DashboardVar(0.5)

        // positions, all in pot ticks
        public var POSITION_OFFSET by DashboardVar(140)

        public var TOP_STOW_POSITION by DashboardVar(0)
        public var BOTTOM_STOW_POSITION by DashboardVar(770)
        public var DEPLOY_POSITION by DashboardVar(360)
        public var HOLD_POSITION by DashboardVar(170)
    }

    object Vision {
        public var CAMERA_HEIGHT by DashboardVar(33.0)
        public var CAMERA_VERTICAL_ANGLE by DashboardVar(0.0)
        public var CAMERA_HORIZONTAL_ANGLE by DashboardVar(7.0)
        public var HATCH_TARGET_HEIGHT by DashboardVar(29.0)
        public var BALL_TARGET_HEIGHT by DashboardVar(36.0)

        public var TARGET_DISTANCE by DashboardVar(24.0) // inches

        public var ACCEPTABLE_ANGLE_ERROR by DashboardVar(3.0) // degrees(?)
        public var ACCEPTABLE_DISTANCE_ERROR by DashboardVar(2.0) // inches (?)

        public var ANGLE_KP by DashboardVar(1.0)
        public var ANGLE_KI by DashboardVar(0.0)
        public var ANGLE_KD by DashboardVar(0.1)
        public var ANGLE_KF by DashboardVar(0.0)

        public var DISTANCE_KP by DashboardVar(0.35)
        public var DISTANCE_KI by DashboardVar(0.0)
        public var DISTANCE_KD by DashboardVar(0.0)
        public var DISTANCE_KF by DashboardVar(0.0)
    }

    object Auto {
        fun initProps() {
            println("init Auto")
        }
        public var AUTO_DELAY by DashboardVar(0.0) // seconds, set this from dashboard
        public var LOOKAHEAD_DISTANCE by DashboardVar(18.0)
    }
}
