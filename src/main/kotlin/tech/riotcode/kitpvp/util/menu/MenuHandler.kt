package tech.riotcode.kitpvp.util.menu

import java.util.*
import java.util.concurrent.ConcurrentHashMap

object MenuHandler {

    val menuMap: MutableMap<UUID, Menu> = ConcurrentHashMap()

    fun removeMenu(uniqueId: UUID) {
        menuMap.remove(uniqueId)
    }

    fun getMenu(uniqueId: UUID): Menu? {
        return menuMap[uniqueId]
    }
}