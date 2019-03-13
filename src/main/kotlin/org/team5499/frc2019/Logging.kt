package org.team5499.frc2019

import org.tinylog.Logger
import edu.wpi.first.wpilibj.PowerDistributionPanel
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotController

import org.team5499.monkeyLib.hardware.LazyTalonSRX
import org.team5499.monkeyLib.hardware.LazyVictorSPX

import org.team5499.frc2019.input.ControlBoard

import org.team5499.frc2019.subsystems.SubsystemsManager

import java.util.concurrent.LinkedBlockingDeque

@SuppressWarnings("MagicNumber", "TooManyFunctions", "MaxLineLength")
object Logging {
    enum class LoggingType {
        ITERATIVE, // iterate through each different set of data(faster)
        DUMP // dump all the data each update loop
    }

    enum class LoggingState {
        PDP,
        PDP_CHANNELS,
        ROBOT_CONTROLLER,
        DRIVER_STATION,
        DRIVETRAIN,
        DRIVER,
        CODRIVER
    }

    @Suppress("ObjectPropertyNaming")
    private val LOGGING_TYPE = LoggingType.DUMP
    @Suppress("ObjectPropertyNaming")
    private var LOGGING_STATE = LoggingState.PDP

    private val logQueue = LinkedBlockingDeque<Pair<String, Any>>(200)
    private val logValues = HashMap<String, Any>()
    private val loggingThread: Thread

    init {
        loggingThread = Thread(LoggingThread())
        loggingThread.start()
    }

    fun update(
        subsystems: SubsystemsManager,
        pdp: PowerDistributionPanel,
        controlBoard: ControlBoard,
        leftMaster: LazyTalonSRX,
        leftSlave1: LazyVictorSPX,
        leftSlave2: LazyVictorSPX,
        rightMaster: LazyTalonSRX,
        rightSlave1: LazyVictorSPX,
        rightSlave2: LazyVictorSPX
    ) {
        when (LOGGING_TYPE) {
            LoggingType.ITERATIVE -> iterate(subsystems, pdp, controlBoard, leftMaster, leftSlave1, leftSlave2, rightMaster, rightSlave1, rightSlave2)
            LoggingType.DUMP -> dump(subsystems, pdp, controlBoard, leftMaster, leftSlave1, leftSlave2, rightMaster, rightSlave1, rightSlave2)
        }
        alwaysLog(subsystems, pdp, controlBoard)
    }

    fun alwaysLog(subsystems: SubsystemsManager, pdp: PowerDistributionPanel, controlBoard: ControlBoard) {
    }

    private fun iterate(
        subsystems: SubsystemsManager,
        pdp: PowerDistributionPanel,
        controlBoard: ControlBoard,
        leftMaster: LazyTalonSRX,
        leftSlave1: LazyVictorSPX,
        leftSlave2: LazyVictorSPX,
        rightMaster: LazyTalonSRX,
        rightSlave1: LazyVictorSPX,
        rightSlave2: LazyVictorSPX
    ) {
        println(LOGGING_STATE)
        LOGGING_STATE = when (LOGGING_STATE) {
            LoggingState.PDP -> { logPDP(pdp); LoggingState.PDP_CHANNELS }
            LoggingState.PDP_CHANNELS -> { logVoltages(leftMaster, leftSlave1, leftSlave2, rightMaster, rightSlave1, rightSlave2); LoggingState.ROBOT_CONTROLLER }
            LoggingState.ROBOT_CONTROLLER -> { logRobotController(); LoggingState.DRIVER_STATION }
            LoggingState.DRIVER_STATION -> { logDriverStation(); LoggingState.DRIVETRAIN }
            LoggingState.DRIVETRAIN -> { logDrivetrain(subsystems); LoggingState.DRIVER }
            LoggingState.DRIVER -> { logDriver(controlBoard); LoggingState.CODRIVER }
            LoggingState.CODRIVER -> { logCodriver(controlBoard); LoggingState.PDP }
        }
    }

    private fun dump(
        subsystems: SubsystemsManager,
        pdp: PowerDistributionPanel,
        controlBoard: ControlBoard,
        leftMaster: LazyTalonSRX,
        leftSlave1: LazyVictorSPX,
        leftSlave2: LazyVictorSPX,
        rightMaster: LazyTalonSRX,
        rightSlave1: LazyVictorSPX,
        rightSlave2: LazyVictorSPX
    ) {
        logPDP(pdp)
        logVoltages(leftMaster, leftSlave1, leftSlave2, rightMaster, rightSlave1, rightSlave2)
        logRobotController()
        logDriverStation()
        logDrivetrain(subsystems)
        logDriver(controlBoard)
        logCodriver(controlBoard)
    }

    private fun logPDP(pdp: PowerDistributionPanel) {
        log("TOTAL_CURRENT", pdp.totalCurrent)
        log("TOTAL_VOLTAGE", pdp.voltage)
    }

    private fun logVoltages(
        leftMaster: LazyTalonSRX,
        leftSlave1: LazyVictorSPX,
        leftSlave2: LazyVictorSPX,
        rightMaster: LazyTalonSRX,
        rightSlave1: LazyVictorSPX,
        rightSlave2: LazyVictorSPX
    ) {
        log("LEFT_MASTER_VOLTAGE", leftMaster.motorOutputVoltage)
        log("LEFT_SLAVE1_VOLTAGE", leftSlave1.motorOutputVoltage)
        log("LEFT_SLAVE2_VOLTAGE", leftSlave2.motorOutputVoltage)
        // log("LEFT_MASTER_CURRENT", leftMaster.getOutputCurrent())
        // log("LEFT_SLAVE1_CURRENT", leftSlave1.getOutputCurrent())
        // log("LEFT_SLAVE2_CURRENT", leftSlave2.getOutputCurrent())

        log("RIGHT_MASTER_VOLTAGE", rightMaster.motorOutputVoltage)
        log("RIGHT_SLAVE1_VOLTAGE", rightSlave1.motorOutputVoltage)
        log("RIGHT_SLAVE2_VOLTAGE", rightSlave2.motorOutputVoltage)
        // log("RIGHT_MASTER_CURRENT", rightMaster.getOutputCurrent())
        // log("RIGHT_SLAVE1_CURRENT", rightSlave1.getOutputCurrent())
        // log("RIGHT_SLAVE2_CURRENT", rightSlave2.getOutputCurrent())
    }

    private fun logRobotController() {
        log("BROWNED_OUT", RobotController.isBrownedOut())
        log("CAN_USAGE", RobotController.getCANStatus().percentBusUtilization)
        log("SYS_ACTIVE", RobotController.isSysActive())
    }

    private fun logDriverStation() {
        log("ALLIANCE", DriverStation.getInstance().alliance)
        log("DS_ATTACHED", DriverStation.getInstance().isDSAttached())
        log("ENABLED", DriverStation.getInstance().isEnabled())
        log("EVENT_NAME", DriverStation.getInstance().eventName)
        log("FMS_ATTACHED", DriverStation.getInstance().isFMSAttached())
        log("GAME_MESSAGE", DriverStation.getInstance().gameSpecificMessage)
        log("DRIVER_LOCATION", DriverStation.getInstance().location)
        log("MATCH_NUMBER", DriverStation.getInstance().matchNumber)
        log("MATCH_TIME", DriverStation.getInstance().matchTime)
        log("MATCH_TYPE", DriverStation.getInstance().matchType)
    }

    private fun logDrivetrain(subsystems: SubsystemsManager) {
        log("POSITION", subsystems.drivetrain.pose.toString())
        log("LVEL", subsystems.drivetrain.leftVelocity.toString())
        log("RVEL", subsystems.drivetrain.rightVelocity.toString())
        log("LVEL_ERR", subsystems.drivetrain.leftVelocityError.toString())
        log("RVEL_ERR", subsystems.drivetrain.leftVelocityError.toString())
    }

    private fun logDriver(controlBoard: ControlBoard) {
        log("DRIVER_STATE", controlBoard.driverControls.getState())
    }

    private fun logCodriver(controlBoard: ControlBoard) {
        log("CODRIVER_STATE", controlBoard.codriverControls.getState())
    }

    private fun log(tag: String, value: Any): Boolean {
        if (logValues.get(tag) != value) {
            logValues.put(tag, value)
            return logQueue.offer(Pair(tag, value))
        } else {
            return true
        }
    }

    class LoggingThread : Runnable {
        override fun run() {
            Thread.currentThread().priority = 1
            while (true) {
                try {
                    println(logQueue.size)
                    val log = logQueue.take()
                    Logger.tag(log.first).trace(log.second)
                } catch (ie: InterruptedException) {
                    println("Logger thread stopped!")
                    Thread.currentThread().interrupt()
                    return
                }
            }
        }
    }
}
