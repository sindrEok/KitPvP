package tech.riotcode.kitpvp.util.menu.item

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.function.BiConsumer

interface MenuItem {

    val item: ItemStack
    val cancelClick: Boolean
    val supportedClickTypes: List<ClickType>
    val clickAction: BiConsumer<ClickType, Player>

}