package org.team5499.frc2019

import org.team5499.dashboard.DashboardVar

@SuppressWarnings("MagicNumber")
public object Constants {

    fun initConstants() {
        println("init Constants")
        Input.initProps()
        Drivetrain.initProps()
        Lift.initProps()
        Intake.initProps()
        Hatch.initProps()
        Auto.initProps()
    }

    public const val ROBOT_UPDATE_PERIOD = 0.02 // maybe change back to 0.005
    public const val TALON_UPDATE_PERIOD_MS = 10
    public const val TALON_PIDF_UPDATE_PERIOD_MS = 1

    public const val EPSILON = 1E-5

    object Input {
        fun initProps() {
            println("init Input")
        }
        // ports
        public const val DRIVER_PORT = 0
        public const val CODRIVER_BUTTON_BOARD_PORT = 1
        public const val CODRIVER_JOYSTICK_PORT = 2

        // driver constants
        public var JOYSTICK_DEADBAND by DashboardVar(0.07)
        public var TURN_MULT by DashboardVar(0.4)
        public var SLOW_MULT by DashboardVar(0.5)
        public var DRIVER_STOW_TIMEOUT by DashboardVar(2.0) // seconds

        // codriver constants
        public var MANUAL_CONTROL_DEADBAND by DashboardVar(0.07)
    }

    object Drivetrain {
        fun initProps() {
            println("init Drivetrain")
        }
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

        public var TURN_KP by DashboardVar(1.5)
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
        fun initProps() {
            println("init Lift")
        }
        // ports
        public const val MASTER_TALON_PORT = 4
        public const val SLAVE_TALON_PORT = 5

        // pid
        public var KP by DashboardVar(0.11) // worked a 0.09
        public var KI by DashboardVar(0.0)
        public var KD by DashboardVar(0.1)
        public var KF by DashboardVar(0.0)

        public var MOTION_MAGIC_VELOCITY by DashboardVar(11000) // 10500 before
        public var MOTION_MAGIC_ACCELERATION by DashboardVar(11000) // 9000 before

        // heights (carriage height in inches)
        public val ROCKET_HOLE_SPACING by DashboardVar(27.0)
        public var STOW_HEIGHT by DashboardVar(3.5)
        public var HATCH_LOW_HEIGHT by DashboardVar(8.0)
        public val HATCH_MID_HEIGHT: Double
            get() = HATCH_LOW_HEIGHT + ROCKET_HOLE_SPACING
        public val HATCH_HIGH_HEIGHT
            get() = HATCH_MID_HEIGHT + ROCKET_HOLE_SPACING + 3.0
        public var BALL_LOW_HEIGHT by DashboardVar(4.5)
        public val BALL_MID_HEIGHT
            get() = BALL_LOW_HEIGHT + ROCKET_HOLE_SPACING
        public val BALL_HIGH_HEIGHT
            get() = BALL_MID_HEIGHT + ROCKET_HOLE_SPACING
        public var BALL_HUMAN_PLAYER_HEIGHT by DashboardVar(20.0)

        // constants
        public const val ENCODER_REDUCTION = 38.0 / 24.0 // reduction from encoder shaft to output shaft
        public const val ENCODER_TICKS_PER_ROTATION = 4096 // of the encoder shaft
        public var MAX_ENCODER_TICKS by DashboardVar((8400 * 4 * ENCODER_REDUCTION).toInt()) // CHANGE THIS, 8100
        public var MIN_ENCODER_TICKS by DashboardVar((400 * ENCODER_REDUCTION).toInt()) // CHANGE THIS
        public var ZEROING_THRESHOLD by DashboardVar(5) // ticks per 100/ms
        public var ZEROING_SPEED by DashboardVar(-0.2) // percent output
        public var ZEROING_TIMEOUT by DashboardVar(0.5) // seconds

        public var MAX_VELOCITY_SETPOINT by DashboardVar(10000) // ticks per 100ms

        public var ACCEPTABLE_VELOCITY_THRESHOLD by DashboardVar(3.0) // inches / s
        public var ACCEPTABLE_DISTANCE_ERROR by DashboardVar(1.0) // inche

        // dimension
        public const val SPROCKET_DIAMETER = 1.23 // inches
        public const val SPROCKET_RADIUS = SPROCKET_DIAMETER / 2.0
        public const val SPROCKET_CIR = SPROCKET_DIAMETER * Math.PI
    }

    object Intake {
        fun initProps() {
            println("init Intake")
        }
        public const val TALON_PORT = 9

        public var INTAKE_SPEED by DashboardVar(0.6)
        public var OUTTAKE_SPEED by DashboardVar(-0.45)
        public var IDLE_SPEED by DashboardVar(0.0)
        public var HOLD_SPEED by DashboardVar(0.2)
    }

    object Hatch {
        fun initProps() {
            println("init Hatch")
        }
        public const val TALON_PORT = 10

        // public const val HATCH_KP = 2.0
        // public const val HATCH_KI = 0.0
        // public const val HATCH_KD = 1.0

        public var KP by DashboardVar(2.0)
        public var KI by DashboardVar(0.0)
        public var KD by DashboardVar(1.0)

        // positions, all in pot ticks
        public var POSITION_OFFSET by DashboardVar(10)

        public var TOP_STOW_POSITION by DashboardVar(0)
        public var BOTTOM_STOW_POSITION by DashboardVar(770)
        public var DEPLOY_POSITION by DashboardVar(385)
        public var HOLD_POSITION by DashboardVar(130)
    }

    object Auto {
        fun initProps() {
            println("init Auto")
        }
        public var LOOKAHEAD_DISTANCE by DashboardVar(18.0)
    }
}
