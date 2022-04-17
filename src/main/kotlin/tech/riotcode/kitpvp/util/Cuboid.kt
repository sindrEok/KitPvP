package tech.riotcode.kitpvp.util

import org.bukkit.Location

data class Cuboid(val minX: Int, val maxX: Int, val minZ: Int, val maxZ: Int) {

    fun contains(location: Location): Boolean = location.x >= minX
            && location.x <= maxX
            && location.z >= minZ
            && location.z <= maxZ

    fun primitiveContains(x: Int, z: Int): Boolean = x in minX..maxX && z in minZ..maxZ
}