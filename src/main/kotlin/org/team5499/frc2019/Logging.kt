package org.team5499.frc2019

import org.tinylog.Logger
import edu.wpi.first.wpilibj.PowerDistributionPanel
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotController

import org.team5499.frc2019.subsystems.SubsystemsManager

object Logging {
    enum class LoggingType {
        ITERATIVE, // iterate through each different set of data(faster)
        DUMP // dump all the data each update loop
    }

    public val LOGGING_TYPE = LoggingType.ITERATIVE

    fun update(subsystems: SubsystemsManager, pdp: PowerDistributionPanel) {
        when (LOGGING_TYPE) {
            ITERATIVE -> iterate(subsystems, pdp)
            DUMP -> dump(subsystems, pdp)
        }
    }

    private fun iterate(subsystems: SubsystemsManager, pdp: PowerDistributionPanel) {
    }

    private fun dump(subsystems: SubsystemsManager, pdp: PowerDistributionPanel) {
        logPDP(pdp)
        logPDPChannels(pdp)
        logRobotController()
        logDriverStation()
        logDrivetrain(subsystems)
    }

    private fun logPDP(pdp: PowerDistributionPanel) {
        Logger.tag("TOTAL_CURRENT").trace(pdp.totalCurrent)
        Logger.tag("TOTAL_VOLTAGE").trace(pdp.voltage)
        Logger.tag("TOTAL_POWER").trace(pdp.totalPower)
    }

    private fun logPDPChannels(pdp: PowerDistributionPanel) {
        Logger.tag("PDP0").trace(pdp.getCurrent(0))
        Logger.tag("PDP1").trace(pdp.getCurrent(1))
        Logger.tag("PDP2").trace(pdp.getCurrent(2))
        Logger.tag("PDP3").trace(pdp.getCurrent(3))
        Logger.tag("PDP4").trace(pdp.getCurrent(4))
        Logger.tag("PDP5").trace(pdp.getCurrent(5))
        Logger.tag("PDP6").trace(pdp.getCurrent(6))
        Logger.tag("PDP7").trace(pdp.getCurrent(7))
        Logger.tag("PDP8").trace(pdp.getCurrent(8))
        Logger.tag("PDP9").trace(pdp.getCurrent(9))
        Logger.tag("PDP10").trace(pdp.getCurrent(10))
        Logger.tag("PDP11").trace(pdp.getCurrent(11))
        Logger.tag("PDP12").trace(pdp.getCurrent(12))
        Logger.tag("PDP13").trace(pdp.getCurrent(13))
        Logger.tag("PDP14").trace(pdp.getCurrent(14))
        Logger.tag("PDP15").trace(pdp.getCurrent(15))
    }

    private fun logRobotController() {
        Logger.tag("BROWNED_OUT").trace(RobotController.isBrownedOut())
        Logger.tag("CAN_USAGE").trace(RobotController.getCANStatus().percentBusUtilization)
        Logger.tag("SYS_ACTIVE").trace(RobotController.isSysActive())
    }

    private fun logDriverStation() {
        Logger.tag("ALLIANCE").trace(DriverStation.getInstance().alliance)
        Logger.tag("DS_ATTACHED").trace(DriverStation.getInstance().isDSAttached())
        Logger.tag("ENABLED").trace(DriverStation.getInstance().isEnabled())
        Logger.tag("EVENT_NAME").trace(DriverStation.getInstance().eventName)
        Logger.tag("FMS_ATTACHED").trace(DriverStation.getInstance().isFMSAttached())
        Logger.tag("GAME_MESSAGE").trace(DriverStation.getInstance().gameSpecificMessage)
        Logger.tag("DRIVER_LOCATION").trace(DriverStation.getInstance().location)
    }

    private fun logDrivetrain(subsystems: SubsystemsManager) {
    }
}
