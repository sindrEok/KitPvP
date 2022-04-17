package tech.riotcode.kitpvp.event.impl.sumo.command

import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.EventHandler
import tech.riotcode.kitpvp.event.EventHandler.arenaMap
import tech.riotcode.kitpvp.event.impl.sumo.SumoEvent
import tech.riotcode.kitpvp.util.ItemBuilder
import tech.riotcode.kitpvp.util.command.CommandClass
import tech.riotcode.kitpvp.util.command.CommandData
import tech.riotcode.kitpvp.util.command.annotate.Command
import tech.riotcode.kitpvp.util.menu.impl.SimpleMenu
import tech.riotcode.kitpvp.util.menu.item.impl.MenuItemBuilder
import kotlin.math.ceil

class SumoHostCommand : CommandClass() {

    val sumoEvent: SumoEvent = EventHandler.findEvent(EventEnum.SUMO) as SumoEvent

    @Command(["sumo.host"], playersOnly = true)
    fun host(commandData: CommandData) {
        SimpleMenu(
            "Choose Sumo Map",
            ceil(arenaMap.size / 9.0).toInt(),
            commandData.getPlayer()
        ).apply {
            menuItems = arenaMap.values.withIndex().associate {
                it.index to MenuItemBuilder(
                    ItemBuilder(it.value.icon.clone()).name(it.value.name).build(),
                    { _, player ->
                        sumoEvent.currentArena = it.value
                        sumoEvent.host(player)
                        sumoEvent.addPlayer(player.uniqueId)
                        player.closeInventory()
                    }
                )
            }
        }.open()
    }

}