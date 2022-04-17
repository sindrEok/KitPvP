package tech.riotcode.kitpvp.event.sticky

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import tech.riotcode.kitpvp.KitpvpPlugin.Companion.plugin

object StickyHandler {

    private val stickyMap: MutableMap<StickyPlayer, Double> = HashMap()

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, { tick() }, 0, 4)
    }

    private fun tick() {
        stickyMap.forEach { (stickyPlayer, time) ->
            if (time <= 0.0) {
                stickyMap.remove(stickyPlayer)
                return
            } else {
                stickyPlayer.player.teleport(stickyPlayer.location)
                stickyMap[stickyPlayer] = time - 0.20
            }
        }
    }

    fun addPlayer(player: Player, location: Location, time: Int) =
        stickyMap.put(StickyPlayer(player, location), time.toDouble())
}