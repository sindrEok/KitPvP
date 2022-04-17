package tech.riotcode.kitpvp.util.menu.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerDropItemEvent
import tech.riotcode.kitpvp.util.menu.MenuHandler

object MenuListener : Listener {

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val menu = MenuHandler.getMenu(event.whoClicked.uniqueId) ?: return

        menu.menuItems.entries.firstOrNull { it.key == event.slot }
            ?.value?.let {
                if (it.supportedClickTypes.contains(event.click))
                    it.clickAction.accept(event.click, event.whoClicked as Player)

                if (it.cancelClick)
                    event.isCancelled = true
            }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDrop(event: PlayerDropItemEvent) {
        MenuHandler.getMenu(event.player.uniqueId) ?: return

        event.isCancelled = true
    }

    @EventHandler
    fun close(event: InventoryCloseEvent) {
        val menuManager = MenuHandler
        if (menuManager.getMenu(event.player.uniqueId) != null)
            menuManager.removeMenu(event.player.uniqueId)
    }

}