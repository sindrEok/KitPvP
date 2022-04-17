package tech.riotcode.kitpvp.util.menu

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import tech.riotcode.kitpvp.KitpvpPlugin
import tech.riotcode.kitpvp.util.menu.item.MenuItem

abstract class Menu {

    private val plugin: KitpvpPlugin = JavaPlugin.getPlugin(KitpvpPlugin::class.java)

    abstract val title: String
    abstract val rows: Int
    abstract val menuItems: Map<Int, MenuItem>
    abstract val player: Player

    fun open() {
        val manager = MenuHandler

        manager.menuMap[player.uniqueId] = this

        val inventory = Bukkit.createInventory(player, rows * 9, title)

        menuItems.forEach { (slot, menuItem) ->
            inventory.setItem(slot, menuItem.item)
        }
        player.openInventory(inventory)
    }

    fun refresh() {
        val inventory = player.openInventory.topInventory

        inventory.clear()

        menuItems.forEach { (slot, menuItem) ->
            inventory.setItem(slot, menuItem.item)
        }
        player.updateInventory()
    }
}