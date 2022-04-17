package tech.riotcode.kitpvp.profile.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object SoupListener : Listener {

    @EventHandler
    fun onSoup(event: PlayerInteractEvent) {
        if (event.item == null || !(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) return

        val player = event.player

        if (event.item.type == Material.MUSHROOM_SOUP && player.health != player.maxHealth) {
            player.health = (player.health + 7F).coerceAtMost(player.maxHealth)
            player.itemInHand.type = Material.BOWL
            player.updateInventory()
        }
    }
}