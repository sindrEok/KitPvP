package tech.riotcode.kitpvp.event.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.EventType
import tech.riotcode.kitpvp.extensions.profile

object EventListener : Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (tech.riotcode.kitpvp.event.EventHandler.activeEvent == null) {
            return
        }
        val profile = event.player.profile

        val enum = profile.currentEvent

        if (enum == EventEnum.NONE) {
            return
        }

        if (event.player.itemInHand.isSimilar(EventType.leaveItem)) {
            tech.riotcode.kitpvp.event.EventHandler.activeEvent!!.leavePlayer(profile)
        }
    }

}