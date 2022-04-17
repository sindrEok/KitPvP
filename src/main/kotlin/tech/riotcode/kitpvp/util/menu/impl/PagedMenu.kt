package tech.riotcode.kitpvp.util.menu.impl

import org.bukkit.entity.Player
import tech.riotcode.kitpvp.util.menu.Menu
import tech.riotcode.kitpvp.util.menu.item.MenuItem
import tech.riotcode.kitpvp.util.menu.item.impl.PageSwitchItem
import kotlin.math.ceil

open class PagedMenu(
    override var rows: Int,
    override var player: Player
) : Menu() {

    override val menuItems: Map<Int, MenuItem> get() = getPageItems()
    override val title: String get() = getMenuTitle()

    private val totalPages: Int get() = getPageSize()

    @Volatile
    var page = 1

    private val from = 9
    private val to = getSize()


    open fun switchPage(forward: Boolean) {
        if (forward) {
            if (page + 1 > getPageSize())
                return

            page += 1
        } else {
            if (page - 1 == 0)
                return

            page -= 1
        }
        player.closeInventory()
        open()
    }

    open fun getPageSize(): Int {
        val size = getItems().size

        return if (size == 0) 1 else ceil(size / (getSize() - 9F)).toInt()
    }

    open fun getItems(): List<MenuItem> {
        return emptyList()
    }

    open fun getPageItems(): Map<Int, MenuItem> {
        val map: MutableMap<Int, MenuItem> = mutableMapOf(
            0 to PageSwitchItem(false, this),
            8 to PageSwitchItem(true, this)
        )
        val items = getItems()
        val size = items.size

        for (i in 0 until size) {
            val slot = if (page == totalPages) i - (page - 1) * from else i - (page - 1) * from + 9

            if (slot in from until to)
                map[slot] = items[i]
        }
        return map
    }

    open fun getMenuTitle(): String {
        return "Page: $page/$totalPages"
    }

    private fun getSize(): Int = rows * 9
}