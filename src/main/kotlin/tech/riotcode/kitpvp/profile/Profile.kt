package tech.riotcode.kitpvp.profile

import com.google.gson.annotations.Expose
import org.bukkit.entity.Player
import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.kit.Kit
import tech.riotcode.kitpvp.lobby.LobbyHandler
import tech.riotcode.kitpvp.profile.killstreak.KillstreakHandler
import java.lang.ref.WeakReference
import java.util.*

data class Profile(@Expose val uuid: UUID) {

    @Expose
    val money: Int = 0

    var personalCoinMultiplier: Float = 1F

    val getCoinMultiplier: Float
        get() = personalCoinMultiplier * ProfileHandler.globalCoinMultiplier

    val killstreak: Int
        get() = KillstreakHandler.findKillstreak(uuid) ?: 0

    @Expose
    var kills: Int = 0

    var buildMode: Boolean = false

    var loadedData = false

    val inWarzone: Boolean
        get() = player.get()
            ?.let {
                !LobbyHandler.lobbyCuboid.contains(it.location) && currentEvent != EventEnum.NONE
            }
            ?: false

    var player: WeakReference<Player> = WeakReference(null)

    var currentEvent: EventEnum = EventEnum.NONE
    var currentKit: Kit? = null
}