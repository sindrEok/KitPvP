package tech.riotcode.kitpvp.event.impl.sumo.command

import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.EventHandler
import tech.riotcode.kitpvp.util.command.CommandClass
import tech.riotcode.kitpvp.util.command.CommandData
import tech.riotcode.kitpvp.util.command.annotate.Command

class SumoJoinCommand : CommandClass() {

    @Command(["sumo.join"], playersOnly = true)
    fun join(commandData: CommandData) {
        val activeEvent = EventHandler.activeEvent
        val player = commandData.getPlayer()

        if (activeEvent == null) {
            player.sendMessage("There is no active event.")
            return
        }
        if (activeEvent.hasStarted) {
            player.sendMessage("The Event is currently ongoing!")
            return
        }
        if (activeEvent.players.contains(player.uniqueId)) {
            player.sendMessage("You are already in the event, bozo")
            return
        }
        if (activeEvent.eventEnum == EventEnum.SUMO) {
            activeEvent.addPlayer(player.uniqueId)
            player.sendMessage("You joined the Sumo event!")
        } else {
            player.sendMessage("This event is not active!")
        }
    }

}