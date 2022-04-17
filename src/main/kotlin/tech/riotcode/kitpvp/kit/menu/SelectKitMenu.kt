package tech.riotcode.kitpvp.kit.menu

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import tech.riotcode.kitpvp.extensions.profile
import tech.riotcode.kitpvp.kit.KitHandler
import tech.riotcode.kitpvp.profile.Profile
import tech.riotcode.kitpvp.util.ItemBuilder
import tech.riotcode.kitpvp.util.menu.impl.SimpleMenu
import tech.riotcode.kitpvp.util.menu.item.impl.MenuItemBuilder

class SelectKitMenu(player: Player) :
    SimpleMenu("Select Kit", kotlin.math.ceil(KitHandler.kitMap.size / 9.0).toInt(), player) {

    init {
        val profile: Profile = player.profile

        menuItems = KitHandler.kitMap.values.mapIndexed { index, kit ->
            index to MenuItemBuilder(
                item = ItemBuilder(kit.icon.clone()).lore("Rick click to purchase").name(kit.toString()).build(),
                clickAction = { clickAction, _ ->
                    if (clickAction == ClickType.LEFT) {
                        kit.apply(profile)
                        player.sendMessage("applied kit $kit")
                        player.closeInventory()
                    } else if (clickAction == ClickType.RIGHT) {
                        // TODO: purchases the kit
                    }
                }
            )
        }.toMap()
    }
}