package tech.riotcode.kitpvp.event.impl.sumo.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.impl.sumo.SumoEvent
import tech.riotcode.kitpvp.extensions.profile

object SumoListener : Listener {

    private val sumoEvent = tech.riotcode.kitpvp.event.EventHandler.findEvent(EventEnum.SUMO) as SumoEvent

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent) {
        val profile = event.player.profile

        if (profile.currentEvent == EventEnum.SUMO) {
            if (sumoEvent.fightingPlayers.contains(profile.uuid)) sumoEvent.killPlayer(profile.uuid)
            else sumoEvent.leavePlayer(profile)
        }
    }


}