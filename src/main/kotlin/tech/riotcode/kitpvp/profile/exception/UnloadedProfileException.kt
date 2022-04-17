package tech.riotcode.kitpvp.profile.exception

import org.bukkit.entity.Player

class UnloadedProfileException(val player: Player) : Exception("${player.displayName}'s profile has not loaded") {

    override fun printStackTrace() {
        player.kickPlayer("Your profile has not loaded")
        super.printStackTrace()
    }

}