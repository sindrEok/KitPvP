package tech.riotcode.kitpvp.util.menu.item.impl

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.util.menu.item.MenuItem
import java.util.function.BiConsumer

data class MenuItemBuilder(
    override val item: ItemStack,
    override var clickAction: BiConsumer<ClickType, Player> = BiConsumer { _, _ -> },
    override var cancelClick: Boolean = true,
    override var supportedClickTypes: List<ClickType> = ClickType.values().toList()
) : MenuItem