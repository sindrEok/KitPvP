package tech.riotcode.kitpvp.event

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.event.arena.Arena
import tech.riotcode.kitpvp.profile.Profile
import tech.riotcode.kitpvp.profile.ProfileHandler
import tech.riotcode.kitpvp.util.CC
import tech.riotcode.kitpvp.util.ItemBuilder
import java.util.*

abstract class EventType {

    companion object {
        val leaveItem = ItemBuilder(Material.INK_SACK).name(CC.of("&cLeave Event")).durability(1).build()
    }

    abstract var hasStarted: Boolean

    abstract var currentArena: Arena

    abstract val eventEnum: EventEnum

    abstract val eventItems: Map<ItemStack, Int>

    abstract val players: MutableList<UUID>

    abstract fun host(player: Player)

    abstract fun start()
    abstract fun stop()

    fun giveItems(player: Player) {
        eventItems.forEach { (item, slot) ->
            player.inventory.setItem(slot, item)
        }
    }

    fun leavePlayer(profile: Profile) {
        val player: Player = profile.player.get() ?: throw Exception("Player is not online.")
        players.remove(profile.uuid)
        // player.teleport() will do later
        player.inventory.clear()
        player.sendMessage(CC.of("&cYou left the event!"))
    }

    fun addPlayer(uuid: UUID) {
        val profile = ProfileHandler.findProfile(uuid)
        val player: Player = profile.player.get() ?: throw Exception("Player is not online.")
        players.add(profile.uuid)
        player.teleport(currentArena.lobbyLocation)
        player.inventory.clear()
        player.inventory.setItem(8, leaveItem)
    }

}