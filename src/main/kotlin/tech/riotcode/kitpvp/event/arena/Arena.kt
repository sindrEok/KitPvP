package tech.riotcode.kitpvp.event.arena

import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.event.EventEnum

data class Arena(
    val name: String,
    var lobbyLocation: Location,
    var spawnLocation1: Location,
    var spawnLocation2: Location,
    var icon: ItemStack,
    val eventEnum: EventEnum
)