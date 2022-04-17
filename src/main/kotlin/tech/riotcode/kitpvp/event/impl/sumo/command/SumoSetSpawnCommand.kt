package tech.riotcode.kitpvp.event.impl.sumo.command

import tech.riotcode.kitpvp.event.EventHandler.arenaMap
import tech.riotcode.kitpvp.util.command.CommandClass
import tech.riotcode.kitpvp.util.command.CommandData
import tech.riotcode.kitpvp.util.command.annotate.Command

class SumoSetSpawnCommand : CommandClass() {

    @Command(["sumo.setspawn"], playersOnly = true)
    fun setSpawn(commandData: CommandData) {
        val arena = arenaMap[commandData.args[1]]
        val player = commandData.getPlayer()

        if (arena != null) {
            if (commandData.args[2] == "1") {
                arena.spawnLocation1 = player.location
                return
            }; if (commandData.args[2] == "2") {
                arena.spawnLocation2 = player.location
                return
            }
            player.sendMessage("That is not a viable spawnlocation.")
        } else {
            player.sendMessage("That arena does not exist.")
        }
    }

}