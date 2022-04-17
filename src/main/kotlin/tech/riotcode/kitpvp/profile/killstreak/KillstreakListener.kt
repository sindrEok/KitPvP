package tech.riotcode.kitpvp.profile.killstreak

import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import tech.riotcode.kitpvp.util.Messages

object KillstreakListener : Listener {

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity

        KillstreakHandler.findKillstreak(player.uniqueId)?.let {
            if (it >= 5) Bukkit.broadcastMessage(
                Messages.KillstreakEndedMessage.formattedMessage
                    .replace("%player%", player.displayName)
                    .replace("%killstreak%", it.toString())
            )
            KillstreakHandler.clearKillstreak(player.uniqueId)
        }
        val killer = (player as LivingEntity).killer

        if (killer != null) {
            val addKillstreak = KillstreakHandler.addKillstreak(killer.uniqueId)

            addKillstreak?.let {
                if (it == 5 || it % 10 == 0) Bukkit.broadcastMessage(
                    Messages.KillstreakReachedMessage.formattedMessage
                        .replace("%player%", killer.displayName)
                        .replace("%killstreak%", it.toString())
                )
            }
        }
    }
}