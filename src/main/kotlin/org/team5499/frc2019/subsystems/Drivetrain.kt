package org.team5499.frc2019.subsystems

import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.SensorTerm
import com.ctre.phoenix.motorcontrol.RemoteSensorSource
import com.ctre.phoenix.motorcontrol.FollowerType
import com.ctre.phoenix.motorcontrol.InvertType
import com.ctre.phoenix.ParamEnum
import com.ctre.phoenix.motorcontrol.StatusFrame

import org.team5499.frc2019.Constants

import org.team5499.monkeyLib.Subsystem
import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.hardware.LazyVictorSPX
import org.team5499.monkeyLib.math.Position
import org.team5499.monkeyLib.math.geometry.Vector2
import org.team5499.monkeyLib.math.geometry.Rotation2d
import org.team5499.monkeyLib.math.geometry.Pose2d
import org.team5499.monkeyLib.util.Utils
import org.team5499.monkeyLib.input.DriveSignal

import org.team5499.dashboard.Dashboard

@Suppress("LargeClass", "TooManyFunctions")
public class Drivetrain(
    leftMaster: LazyTalonSRX,
    leftSlave1: LazyVictorSPX,
    leftSlave2: LazyVictorSPX,
    rightMaster: LazyTalonSRX,
    rightSlave1: LazyVictorSPX,
    rightSlave2: LazyVictorSPX,
    gyro: PigeonIMU
) : Subsystem() {

    private enum class DriveMode {
        OPEN_LOOP,
        POSITION,
        VELOCITY,
        TURN
    }

    // hardware
    private val mLeftMaster: LazyTalonSRX
    private val mLeftSlave1: LazyVictorSPX
    private val mLeftSlave2: LazyVictorSPX

    private val mRightMaster: LazyTalonSRX
    private val mRightSlave1: LazyVictorSPX
    private val mRightSlave2: LazyVictorSPX

    private val mGyro: PigeonIMU

    // drive variables
    private var mDriveMode: DriveMode = DriveMode.OPEN_LOOP
        set(value) {
            if (field == value) return
            when (value) {
                DriveMode.OPEN_LOOP -> {
                    configureForPercent()
                }
                DriveMode.POSITION -> {
                    configureForPosition()
                }
                DriveMode.VELOCITY -> {
                    configureForVelocity()
                }
                DriveMode.TURN -> {
                    configureForTurn()
                }
            }
            field = value
        }

    public var brakeMode: Boolean = false
        set(value) {
            if (value == field) return
            val mode = if (value) NeutralMode.Brake else NeutralMode.Coast
            mLeftMaster.setNeutralMode(mode)
            mLeftSlave1.setNeutralMode(mode)
            mLeftSlave2.setNeutralMode(mode)

            mRightMaster.setNeutralMode(mode)
            mRightSlave1.setNeutralMode(mode)
            mRightSlave2.setNeutralMode(mode)
            field = value
        }

    private val mPosition = Position()

    public fun setPosition(vector: Vector2) {
        mPosition.positionVector = vector
    }

    private var mGyroOffset: Rotation2d = Rotation2d.identity
        set(value) { field = value }

    public var heading: Rotation2d
        get() {
            return Rotation2d.fromDegrees(mGyro.getFusedHeading()).rotateBy(mGyroOffset)
            // return Rotation2d.fromDegrees(mGyro.getFusedHeading())
        }
        set(value) {
            println("SET HEADING: ${heading.degrees}")
            mGyroOffset = value.rotateBy(Rotation2d.fromDegrees(mGyro.getFusedHeading()).inverse())
            // mGyro.setFusedHeading(value.degrees, 0)
            println("Gyro offset: ${mGyroOffset.degrees}")
        }

    public val angularVelocity: Double
        get() {
            val xyz = doubleArrayOf(0.0, 0.0, 0.0)
            mGyro.getRawGyro(xyz)
            return xyz[1]
        }

    public val pose: Pose2d
        get() = Pose2d(mPosition.positionVector, heading)

    public var leftDistance: Double
        get() {
            return -Utils.encoderTicksToInches(
                Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                Constants.Drivetrain.WHEEL_CIR,
                mLeftMaster.sensorCollection.quadraturePosition
            )
        }
        set(inches) {
            mLeftMaster.sensorCollection.setQuadraturePosition(
                Utils.inchesToEncoderTicks(Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                Constants.Drivetrain.WHEEL_CIR,
                inches), 0)
        }

    public var rightDistance: Double
        get() {
            return Utils.encoderTicksToInches(
                Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                Constants.Drivetrain.WHEEL_CIR,
                mRightMaster.sensorCollection.quadraturePosition
            )
        }
        set(inches) {
            mRightMaster.getSensorCollection().setQuadraturePosition(
                Utils.inchesToEncoderTicks(
                    Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                    Constants.Drivetrain.WHEEL_CIR,
                    inches
                ), 0)
        }

    public val leftVelocity: Double
        get() = -Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
            Constants.Drivetrain.WHEEL_CIR,
            mLeftMaster.sensorCollection.quadratureVelocity
        )

    public val rightVelocity: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
            Constants.Drivetrain.WHEEL_CIR,
            mRightMaster.sensorCollection.quadratureVelocity
        )

    public val averageVelocity: Double
        get() = (leftVelocity + rightVelocity) / 2.0

    public val positionError: Double
        get() = Utils.encoderTicksToInches(
            Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
            Constants.Drivetrain.WHEEL_CIR,
            mRightMaster.getClosedLoopError(0)
        )

    public val leftVelocityError: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
            Constants.Drivetrain.WHEEL_CIR,
            mLeftMaster.getClosedLoopError(0)
        )

    public val rightVelocityError: Double
        get() = Utils.encoderTicksPer100MsToInchesPerSecond(
            Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
            Constants.Drivetrain.WHEEL_CIR,
            mRightMaster.getClosedLoopError(0)
        )

    public val averageVelocityError: Double
        get() = (leftVelocityError + rightVelocityError) / 2.0

    public val anglePositionError: Double
        get() = Utils.talonAngleToDegrees(
            Constants.Drivetrain.TURN_UNITS_PER_ROTATION,
            mRightMaster.getClosedLoopError(1)
        )

    public val turnError: Double
        get() = Utils.talonAngleToDegrees(
            Constants.Drivetrain.TURN_UNITS_PER_ROTATION,
            mRightMaster.getClosedLoopError(0)
        )

    public val turnFixedError: Double
        get() = Utils.encoderTicksToInches(
            Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
            Constants.Drivetrain.WHEEL_CIR,
            mRightMaster.getClosedLoopError(1)
        )

    init {
        // initialze hardware
        mLeftMaster = leftMaster.apply {
            setInverted(false)
            setSensorPhase(true)
            setStatusFramePeriod(
                StatusFrameEnhanced.Status_3_Quadrature,
                Constants.TALON_UPDATE_PERIOD_MS,
                0
            )
        }
        mLeftSlave1 = leftSlave1.apply {
            follow(mLeftMaster)
            setInverted(InvertType.FollowMaster)
        }
        mLeftSlave2 = leftSlave2.apply {
            follow(mLeftMaster)
            setInverted(InvertType.FollowMaster)
        }

        mRightMaster = rightMaster.apply {
            setInverted(true)
            setSensorPhase(true)
            setStatusFramePeriod(
                StatusFrameEnhanced.Status_3_Quadrature,
                Constants.TALON_UPDATE_PERIOD_MS,
                0
            )
        }
        mRightSlave1 = rightSlave1.apply {
            follow(mRightMaster)
            setInverted(InvertType.FollowMaster)
        }
        mRightSlave2 = rightSlave2.apply {
            follow(mRightMaster)
            setInverted(InvertType.FollowMaster)
        }

        mGyro = gyro

        setCallbacks()
    }

    @Suppress("ComplexMethod")
    private fun setCallbacks() {
        /* set callbacks */
        // velocity PIDF
        Dashboard.addInlineListener("Constants.Drivetrain.VEL_KP") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.VELOCITY) {
                mLeftMaster.config_kP(0, value, 0)
                mRightMaster.config_kP(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.VEL_KI") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.VELOCITY) {
                mLeftMaster.config_kI(0, value, 0)
                mRightMaster.config_kI(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.VEL_KD") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.VELOCITY) {
                mLeftMaster.config_kD(0, value, 0)
                mRightMaster.config_kD(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.VEL_KF") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.VELOCITY) {
                mLeftMaster.config_kF(0, value, 0)
                mRightMaster.config_kF(0, value, 0)
            }
        }

        // position PID
        Dashboard.addInlineListener("Constants.Drivetrain.POS_KP") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.POSITION) {
                mRightMaster.config_kP(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.POS_KI") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.POSITION) {
                mRightMaster.config_kI(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.POS_KD") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.POSITION) {
                mRightMaster.config_kD(0, value, 0)
            }
        }

        // angle PID
        Dashboard.addInlineListener("Constants.Drivetrain.ANGLE_KP") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.POSITION) {
                mRightMaster.config_kP(1, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.ANGLE_KI") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.POSITION) {
                mRightMaster.config_kI(1, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.ANGLE_KD") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.POSITION) {
                mRightMaster.config_kD(1, value, 0)
            }
        }

        // turn PID
        Dashboard.addInlineListener("Constants.Drivetrain.TURN_KP") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.TURN) {
                mRightMaster.config_kP(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.TURN_KI") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.TURN) {
                mRightMaster.config_kI(0, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.TURN_KD") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.TURN) {
                mRightMaster.config_kD(0, value, 0)
            }
        }

        // fixed PID
        Dashboard.addInlineListener("Constants.Drivetrain.FIXED_KP") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.TURN) {
                mRightMaster.config_kP(1, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.FIXED_KI") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.TURN) {
                mRightMaster.config_kI(1, value, 0)
            }
        }
        Dashboard.addInlineListener("Constants.Drivetrain.FIXED_KD") {
            _: String, value: Double? ->
            if (value != null && mDriveMode == DriveMode.TURN) {
                mRightMaster.config_kD(1, value, 0)
            }
        }
    }

    public fun setPercent(signal: DriveSignal) {
        setPercent(signal.left, signal.right, signal.brakeMode)
    }

    public fun setPercent(left: Double, right: Double, brakeMode: Boolean = false) {
        mDriveMode = DriveMode.OPEN_LOOP
        mLeftMaster.set(ControlMode.PercentOutput, left)
        mRightMaster.set(ControlMode.PercentOutput, right)
        this.brakeMode = brakeMode
    }

    public fun setPosition(distance: Double) {
        mDriveMode = DriveMode.POSITION
        val absDistance = Utils.inchesToEncoderTicks(
            Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
            Constants.Drivetrain.WHEEL_CIR,
            ((leftDistance + rightDistance) / 2.0) + distance
        )
        val angleTarget = mRightMaster.getSelectedSensorPosition(1)
        mRightMaster.set(ControlMode.MotionMagic, absDistance.toDouble(), DemandType.AuxPID, angleTarget.toDouble())
    }

    public fun setTurn(angle: Double) {
        mDriveMode = DriveMode.TURN
        val fixedDistance = Utils.inchesToEncoderTicks(
            Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
            Constants.Drivetrain.WHEEL_CIR,
            (leftDistance + rightDistance) / 2.0
        )
        val angleTarget = 0.0 // mRightMaster.getSelectedSensorPosition(1) +
            // Utils.degreesToTalonAngle(Constants.Drivetrain.TURN_UNITS_PER_ROTATION, angle - mGyroOffset.degrees)
        mRightMaster.set(ControlMode.Position, angleTarget.toDouble(), DemandType.AuxPID, fixedDistance.toDouble())
    }

    public fun setVelocity(leftSpeed: Double, rightSpeed: Double) {
        mDriveMode = DriveMode.VELOCITY
        val left = Utils.limit(leftSpeed, Constants.Drivetrain.MAX_VELOCITY)
        val right = Utils.limit(rightSpeed, Constants.Drivetrain.MAX_VELOCITY)
        mLeftMaster.set(ControlMode.Velocity,
            Utils.inchesPerSecondToEncoderTicksPer100Ms(
                Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                Constants.Drivetrain.WHEEL_CIR,
                left)
            )
        mRightMaster.set(ControlMode.Velocity,
            Utils.inchesPerSecondToEncoderTicksPer100Ms(
                Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                Constants.Drivetrain.WHEEL_CIR,
                right)
            )
    }

    private fun configureForPercent() {
        // brakeMode = false
        mLeftMaster.set(ControlMode.PercentOutput, 0.0) // make sure its not following right master
        mLeftMaster.apply {
            configNominalOutputForward(0.0, 0)
            configNominalOutputReverse(0.0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setInverted(false)
            setSensorPhase(false)
        }

        mRightMaster.apply {
            configNominalOutputForward(0.0, 0)
            configNominalOutputReverse(0.0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setInverted(true)
            setSensorPhase(false)
        }
    }

    private fun configureForVelocity() {
        // brakeMode = true
        mLeftMaster.set(ControlMode.PercentOutput, 0.0) // make sure its not following rightMaster
        mLeftMaster.apply {
            setInverted(false)
            configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setSensorPhase(true)
            println("COnfig pid constants")
            config_kP(0, Constants.Drivetrain.VEL_KP, 0)
            config_kI(0, Constants.Drivetrain.VEL_KI, 0)
            config_kD(0, Constants.Drivetrain.VEL_KD, 0)
            config_kF(0, Constants.Drivetrain.VEL_KF, 0)
            config_kP(1, 0.0, 0)
            config_kI(1, 0.0, 0)
            config_kD(1, 0.0, 0)
            config_kF(1, 0.0, 0)
            config_IntegralZone(0, 0, 0)
            configClosedLoopPeakOutput(0, 1.0, 0)
            config_IntegralZone(1, 0, 0)
            configClosedLoopPeakOutput(1, 0.0, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
        }

        mRightMaster.apply {
            setInverted(true)
            configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0)
            configRemoteFeedbackFilter(0x00, RemoteSensorSource.Off, 0, 0)
            configRemoteFeedbackFilter(0x00, RemoteSensorSource.Off, 1, 0)
            configSensorTerm(SensorTerm.Sum0, FeedbackDevice.SoftwareEmulatedSensor, 0)
            configSensorTerm(SensorTerm.Sum1, FeedbackDevice.SoftwareEmulatedSensor, 0)
            configSelectedFeedbackCoefficient(1.0, 1, 0)
            configSelectedFeedbackCoefficient(1.0, 0, 0)
            configSelectedFeedbackSensor(FeedbackDevice.Tachometer, 1, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setSensorPhase(true)
            config_kP(0, Constants.Drivetrain.VEL_KP, 0)
            config_kI(0, Constants.Drivetrain.VEL_KI, 0)
            config_kD(0, Constants.Drivetrain.VEL_KD, 0)
            config_kF(0, Constants.Drivetrain.VEL_KF, 0)
            config_kP(1, 0.0, 0)
            config_kI(1, 0.0, 0)
            config_kD(1, 0.0, 0)
            config_kF(1, 0.0, 0)
            config_IntegralZone(0, 0, 0)
            configClosedLoopPeakOutput(0, 1.0, 0)
            config_IntegralZone(1, 0, 0)
            configClosedLoopPeakOutput(1, 0.0, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
        }
    }

    private fun configureForTurn() {
        // brakeMode = true
        mLeftMaster.apply {
            configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, Constants.TALON_UPDATE_PERIOD_MS, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            follow(mRightMaster, FollowerType.AuxOutput1)
            setSensorPhase(false)
            setInverted(true)
        }

        mRightMaster.apply {
            configRemoteFeedbackFilter(mLeftMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, 0)
            configRemoteFeedbackFilter(mGyro.getDeviceID(), RemoteSensorSource.Pigeon_Yaw, 1, 0)
            configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, 0)
            configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, 0)
            configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 1, 0)
            @Suppress("MagicNumber")
            configSelectedFeedbackCoefficient(0.5, 1, 0)
            configSelectedFeedbackCoefficient(
                (Constants.Drivetrain.TURN_UNITS_PER_ROTATION /
                Constants.Drivetrain.PIGEON_UNITS_PER_ROTATION).toDouble(), 0, 0)
            configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, 0, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, Constants.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, Constants.TALON_UPDATE_PERIOD_MS, 0)
            setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, Constants.TALON_UPDATE_PERIOD_MS, 0)
            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)

            config_kP(0, Constants.Drivetrain.TURN_KP, 0)
            config_kI(0, Constants.Drivetrain.TURN_KI, 0)
            config_kD(0, Constants.Drivetrain.TURN_KD, 0)
            config_kF(0, Constants.Drivetrain.TURN_KF, 0)

            config_kP(1, Constants.Drivetrain.FIXED_KP, 0)
            config_kI(1, Constants.Drivetrain.FIXED_KI, 0)
            config_kD(1, Constants.Drivetrain.FIXED_KD, 0)
            config_kF(1, Constants.Drivetrain.FIXED_KF, 0)

            configMotionCruiseVelocity(
                Utils.inchesPerSecondToEncoderTicksPer100Ms(
                    Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                    Constants.Drivetrain.WHEEL_CIR,
                    Constants.Drivetrain.MAX_VELOCITY
                ).toInt()
            )

            configMotionAcceleration(
                Utils.inchesPerSecondToEncoderTicksPer100Ms(
                    Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                    Constants.Drivetrain.WHEEL_CIR,
                    Constants.Drivetrain.ACCEPTABLE_VELOCITY_THRESHOLD
                ).toInt()
            )

            config_IntegralZone(0, Constants.Drivetrain.TURN_IZONE, 0)
            configClosedLoopPeakOutput(0, Constants.Drivetrain.TURN_MAX_OUTPUT, 0)
            config_IntegralZone(1, Constants.Drivetrain.FIXED_IZONE, 0)
            configClosedLoopPeakOutput(1, Constants.Drivetrain.FIXED_MAX_OUTPUT, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 0, 0)
            configSetParameter(ParamEnum.ePIDLoopPeriod,
                Constants.TALON_PIDF_UPDATE_PERIOD_MS.toDouble(), 0x00, 1, 0)
            configAuxPIDPolarity(!Constants.Drivetrain.INVERT_TURN_AUX_PIDF, 0)
            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)
            setSensorPhase(true)
        }
    }

    private fun configureForPosition() {
        mLeftMaster.apply {
            configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0)

            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setInverted(false)
            setSensorPhase(true)

            follow(mRightMaster, FollowerType.AuxOutput1)
        }

        @Suppress("MagicNumber")
        mRightMaster.apply {
            configRemoteFeedbackFilter(mLeftMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, 0)
            configRemoteFeedbackFilter(mGyro.getDeviceID(), RemoteSensorSource.Pigeon_Yaw, 1, 0)
            configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, 0)
            configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, 0)
            configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 0, 0)
            configSelectedFeedbackCoefficient(0.5, 0, 0)
            configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, 1, 0)
            configSelectedFeedbackCoefficient(1.0, 1, 0)
            setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, 0)
            setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, 0)
            setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, 0)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, 0)
            configAuxPIDPolarity(false, 0)

            config_kP(0, Constants.Drivetrain.POS_KP, 0)
            config_kI(0, Constants.Drivetrain.POS_KI, 0)
            config_kD(0, Constants.Drivetrain.POS_KD, 0)
            config_kF(0, Constants.Drivetrain.POS_KF, 0)
            config_IntegralZone(0, Constants.Drivetrain.POS_IZONE, 0)
            configClosedLoopPeakOutput(0, Constants.Drivetrain.POS_MAX_OUTPUT, 0)

            config_kP(1, Constants.Drivetrain.ANGLE_KP, 0)
            config_kP(1, Constants.Drivetrain.ANGLE_KI, 0)
            config_kP(1, Constants.Drivetrain.ANGLE_KD, 0)
            config_kP(1, Constants.Drivetrain.ANGLE_KF, 0)
            config_IntegralZone(1, Constants.Drivetrain.ANGLE_IZONE, 0)
            configClosedLoopPeakOutput(1, Constants.Drivetrain.ANGLE_MAX_OUTPUT, 0)

            configMotionCruiseVelocity(
                Utils.inchesPerSecondToEncoderTicksPer100Ms(
                    Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                    Constants.Drivetrain.WHEEL_CIR,
                    Constants.Drivetrain.MAX_VELOCITY
                ).toInt()
            )

            configMotionAcceleration(
                Utils.inchesPerSecondToEncoderTicksPer100Ms(
                    Constants.Drivetrain.ENCODER_TICKS_PER_ROTATION,
                    Constants.Drivetrain.WHEEL_CIR,
                    Constants.Drivetrain.MAX_ACCELERATION
                ).toInt()
            )

            selectProfileSlot(0, 0)
            selectProfileSlot(1, 1)

            configPeakOutputForward(+1.0, 0)
            configPeakOutputReverse(-1.0, 0)
            setInverted(true)
            setSensorPhase(true)
        }
    }

    public override fun update() {
        mPosition.update(leftDistance, rightDistance, heading.degrees)
        println("turn error: $turnError")
        println("fused: ${mGyro.getFusedHeading()}")
        println("selected position ${mRightMaster.getSelectedSensorPosition(1)}")
    }

    public override fun stop() {
        leftDistance = 0.0
        rightDistance = 0.0
        mPosition.reset()
        brakeMode = false
    }

    public override fun reset() {
        setPercent(0.0, 0.0)
        stop()
        mLeftMaster.neutralOutput()
        mRightMaster.neutralOutput()
        brakeMode = false
    }

    companion object {
        private const val kPrimaryPIDSlot = 0
        private const val kSecondaryPIDSlot = 1
    }
}
