package tech.riotcode.kitpvp.event.impl.sumo.command

import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.EventHandler
import tech.riotcode.kitpvp.util.ItemBuilder
import tech.riotcode.kitpvp.util.command.CommandClass
import tech.riotcode.kitpvp.util.command.CommandData
import tech.riotcode.kitpvp.util.command.annotate.Command
import tech.riotcode.kitpvp.util.menu.impl.SimpleMenu
import tech.riotcode.kitpvp.util.menu.item.impl.MenuItemBuilder
import kotlin.math.ceil

class SumoSetLobbyCommand : CommandClass() {

    @Command(["sumo.setlobby"], playersOnly = true)
    fun setLobby(commandData: CommandData) {
        val arenaList =
            EventHandler.arenaMap.filter { it.value.eventEnum == EventEnum.SUMO }.values.toList()

        SimpleMenu("Choose arena", ceil(arenaList.size / 9F).toInt(), commandData.getPlayer()).apply {
            menuItems = arenaList.mapIndexed { index, arena ->
                index to MenuItemBuilder(
                    item = ItemBuilder(arena.icon).name(arena.name).build(),
                    clickAction = { _, player ->
                        arena.lobbyLocation = player.location
                        player.sendMessage("You set the lobby location!!!!")
                        player.closeInventory()
                    }
                )
            }.toMap()
        }.open()
    }

}