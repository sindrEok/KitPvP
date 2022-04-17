package tech.riotcode.kitpvp.util.menu.impl

import org.bukkit.entity.Player
import tech.riotcode.kitpvp.util.menu.Menu
import tech.riotcode.kitpvp.util.menu.item.MenuItem

open class SimpleMenu(
    override var title: String,
    override var rows: Int,
    override val player: Player
) : Menu() {

    override var menuItems: Map<Int, MenuItem> = emptyMap()
}
