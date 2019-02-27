package org.team5499.frc2019

import org.tinylog.Logger
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.PowerDistributionPanel
import edu.wpi.first.wpilibj.RobotController

object Logging {

    enum class LogState {
        PDP,
        PDP_PORTS,
        DRIVERSTATION,
        PDP_CHANNELS
    }

    var logState = LogState.PDP

    fun logStartupData() {
    }

    fun logMatchData() {
        Logger.tag("ALLIANCE").trace(DriverStation.getInstance().alliance)
        Logger.tag("COMP_NAME").trace(DriverStation.getInstance().eventName as Any)
        Logger.tag("GAME_MESSAGE").trace(DriverStation.getInstance().gameSpecificMessage as Any)
        Logger.tag("LOCATION").trace(DriverStation.getInstance().location)
        Logger.tag("MATCH_NUMBER").trace(DriverStation.getInstance().matchNumber)
        Logger.tag("MATCH_TYPE").trace(DriverStation.getInstance().matchType)
        Logger.tag("REPLAY").trace(DriverStation.getInstance().replayNumber)
    }

    fun logIterativeData(pdp: PowerDistributionPanel) {
        logState = when (logState) {
            LogState.DRIVERSTATION -> {
                Logger.tag("DS_ATTACHED").trace(DriverStation.getInstance().isDSAttached())
                Logger.tag("FMS").trace(DriverStation.getInstance().isFMSAttached())
                LogState.PDP
            }
            LogState.PDP -> {
                Logger.tag("BATTERY").trace(pdp.voltage)
                Logger.tag("POWER").trace(pdp.totalPower)
                Logger.tag("CURRENT").trace(pdp.totalCurrent)
                LogState.PDP_PORTS
            }
            LogState.PDP_PORTS -> {
                logPdpChannels(pdp)
                LogState.PDP_CHANNELS
            }
            LogState.PDP_CHANNELS -> {
                Logger.tag("CAN_USAGE").trace(RobotController.getCANStatus().percentBusUtilization)
                Logger.tag("BROWNED_OUT").trace(RobotController.isBrownedOut())
                Logger.tag("SYS_ACTIVE").trace(RobotController.isSysActive())
                LogState.DRIVERSTATION
            }
        }
    }

    @Suppress("MagicNumber")
    fun logPdpChannels(pdp: PowerDistributionPanel) {
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
}
