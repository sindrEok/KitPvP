package tech.riotcode.kitpvp.util.menu.item.impl

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.util.ItemBuilder
import tech.riotcode.kitpvp.util.menu.impl.PagedMenu
import tech.riotcode.kitpvp.util.menu.item.MenuItem
import java.util.function.BiConsumer

class PageSwitchItem(
    forward: Boolean,
    private val menu: PagedMenu,
    override val cancelClick: Boolean = true,
    override val supportedClickTypes: List<ClickType> = listOf(ClickType.LEFT, ClickType.RIGHT),
) : MenuItem {

    private var clicked = false

    override val item: ItemStack = if (forward) {
        if (hasNextPage()) {
            ItemBuilder(Material.ARROW)
                .name("&7Next Page")
                .lore("&7&oClick this item to", "&7&oswitch to the next page.")
                .build()
        } else {
            ItemBuilder(Material.ARROW)
                .name("&7No Pages Available")
                .lore("&7&oNo next page available.")
                .build()
        }
    } else {
        if (hasPreviousPage()) {
            ItemBuilder(Material.ARROW)
                .name("&7Previous Page")
                .lore("&7&oClick this item to", "&7&oswitch to the previous page.")
                .build()
        } else {
            ItemBuilder(Material.ARROW)
                .name("&7No Pages Available")
                .lore("&7&oNo previous pages available.")
                .build()
        }
    }

    override val clickAction: BiConsumer<ClickType, Player> = BiConsumer { _, player ->
        if (clicked)
            return@BiConsumer

        if (forward && hasNextPage()) {
            menu.switchPage(forward)
            player.playSound(player.location, Sound.NOTE_PLING, 1f, 1f)
            clicked = true
            return@BiConsumer
        }

        if (!forward && hasPreviousPage()) {
            menu.switchPage(forward)
            player.playSound(player.location, Sound.NOTE_PLING, 1f, 1f)
            clicked = true
            return@BiConsumer
        }
    }

    private fun hasNextPage(): Boolean = menu.getPageSize() >= menu.page + 1

    private fun hasPreviousPage(): Boolean = menu.page - 1 > 0
}