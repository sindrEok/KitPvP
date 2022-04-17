package tech.riotcode.kitpvp.profile.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.extensions.profile
import tech.riotcode.kitpvp.kit.KitHandler
import tech.riotcode.kitpvp.lobby.LobbyHandler
import tech.riotcode.kitpvp.profile.Profile
import tech.riotcode.kitpvp.profile.ProfileHandler
import java.lang.ref.WeakReference

object ProfileListener : Listener {


    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) {
        val uniqueId = event.player.uniqueId
        val profile = Profile(uniqueId)

        ProfileHandler.profileMap[uniqueId] = profile
        ProfileHandler.loadProfile(profile, true)

        profile.player = WeakReference(event.player)

        LobbyHandler.lobbyPlayer(profile)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val profile = event.player.profile

        profile.player.clear()
        profile.currentKit = null
        profile.currentEvent = EventEnum.NONE

        KitHandler.kitCooldown.removeCooldown(profile.uuid)
    }

}