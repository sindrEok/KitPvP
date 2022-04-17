package tech.riotcode.kitpvp.extensions

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import tech.riotcode.kitpvp.profile.Profile
import tech.riotcode.kitpvp.profile.ProfileHandler
import java.util.*

val Player.profile: Profile
    get() = ProfileHandler.findProfile(this.uniqueId)

val UUID.profile: Profile
    get() = ProfileHandler.findProfile(this)

val UUID.player: Player
    get() = Bukkit.getPlayer(this)