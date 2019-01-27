package org.team5499.frc2019

import org.team5499.dashboard.Dashboard
import kotlin.reflect.KProperty
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.KVisibility

@SuppressWarnings("MagicNumber")
public object Constants {

    fun initConstants() {
        initClassProps(Constants::class)
    }

    private fun initClassProps(clazz: KClass<*>) {
        clazz.memberProperties.forEach({
            if (it.visibility == KVisibility.PUBLIC) {
                println("${it.name}")
                it.getter.call(clazz)
            }
            // println("$clazz : ${it.get(clazz)}")
        })
        clazz.nestedClasses.forEach({
            initClassProps(it)
        })
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

        public var VEL_KP by DashboardDelegate(2.5)
        public var VEL_KI by DashboardDelegate(0.0)
        public var VEL_KD by DashboardDelegate(0.0)
        public var VEL_KF by DashboardDelegate(0.95)
        public var VEL_IZONE by DashboardDelegate(10)
        public var VEL_MAX_OUTPUT by DashboardDelegate(1.0)

        public var POS_KP by DashboardDelegate(0.79)
        public var POS_KI by DashboardDelegate(0.0)
        public var POS_KD by DashboardDelegate(0.3)
        public var POS_KF by DashboardDelegate(0.0)
        public var POS_IZONE by DashboardDelegate(10)
        public var POS_MAX_OUTPUT by DashboardDelegate(0.5)

        public var ANGLE_KP by DashboardDelegate(2.0)
        public var ANGLE_KI by DashboardDelegate(0.0)
        public var ANGLE_KD by DashboardDelegate(0.0)
        public var ANGLE_KF by DashboardDelegate(0.0)
        public var ANGLE_IZONE by DashboardDelegate(10)
        public var ANGLE_MAX_OUTPUT by DashboardDelegate(1.0)

        public var TURN_KP by DashboardDelegate(1.3)
        public var TURN_KI by DashboardDelegate(0.0)
        public var TURN_KD by DashboardDelegate(12.0)
        public var TURN_KF by DashboardDelegate(0.0)
        public var TURN_IZONE by DashboardDelegate(10)
        public var TURN_MAX_OUTPUT by DashboardDelegate(1.0)

        public var FIXED_KP by DashboardDelegate(0.1)
        public var FIXED_KI by DashboardDelegate(0.0)
        public var FIXED_KD by DashboardDelegate(0.0)
        public var FIXED_KF by DashboardDelegate(0.0)
        public var FIXED_IZONE by DashboardDelegate(10)
        public var FIXED_MAX_OUTPUT by DashboardDelegate(0.5)

        public var INVERT_FIXED_AUX_PIDF by DashboardDelegate(true)
        public var INVERT_ANGLE_AUX_PIDF by DashboardDelegate(true)
        public var INVERT_TURN_AUX_PIDF by DashboardDelegate(false)
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

    class DashboardDelegate<T>(var initValue: T) {

        var isInit = false

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            if (!isInit) {
                Dashboard.setVariable(property.name, initValue as Any)
                return initValue
            }
            return Dashboard.getVariable(property.name)
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            Dashboard.setVariable(property.name, value as Any)
        }
    }
}
