package tech.riotcode.kitpvp.event.impl.sumo.command

import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.EventHandler
import tech.riotcode.kitpvp.event.impl.sumo.SumoEvent
import tech.riotcode.kitpvp.util.command.CommandClass
import tech.riotcode.kitpvp.util.command.CommandData
import tech.riotcode.kitpvp.util.command.annotate.Command

class SumoCreateCommand : CommandClass() {

    private val sumoEvent = EventHandler.findEvent(EventEnum.SUMO) as SumoEvent

    @Command(["sumo.create"])
    fun create(commandData: CommandData) {
        sumoEvent.createArena(commandData.args[0], EventEnum.SUMO)
        commandData.sender.sendMessage("Created a new arena if already not existing.")
    }

}