package tech.riotcode.kitpvp.event.sticky

import org.bukkit.Location
import org.bukkit.entity.Player

data class StickyPlayer(val player: Player, val location: Location)