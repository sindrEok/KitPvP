package tech.riotcode.kitpvp.event.impl.sumo.task

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import tech.riotcode.kitpvp.event.impl.sumo.SumoEvent

class DeathTask(private val sumoEvent: SumoEvent) : BukkitRunnable() {

    var avoidException: Boolean = false

    override fun run() {
        if (sumoEvent.fightingPlayers.size == 0 || avoidException) return

        sumoEvent.fightingPlayers
            .map { Bukkit.getPlayer(it) }
            .firstOrNull { it.location.block.isLiquid || it.location.y <= 0 }
            ?.let {
                avoidException = true
                sumoEvent.killPlayer(it.uniqueId)
            }
    }
}