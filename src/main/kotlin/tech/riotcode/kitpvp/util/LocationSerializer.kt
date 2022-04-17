package tech.riotcode.kitpvp.util

import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

/*
 * Author: idk? not me
 */

object LocationSerializer {
    fun getFaces(start: Location): Array<Location?> {
        val faces: Array<Location?> = arrayOfNulls(4)
        faces[0] = Location(start.world, start.x + 1, start.y, start.z)
        faces[1] = Location(start.world, start.x - 1, start.y, start.z)
        faces[2] = Location(start.world, start.x, start.y + 1, start.z)
        faces[3] = Location(start.world, start.x, start.y - 1, start.z)
        return faces
    }

    fun serialize(location: Location?): String {
        return if (location == null) {
            "null"
        } else Objects.requireNonNull(location.world).name
            .toString() + ":" + location.x + ":" + location.y + ":" + location.z +
                ":" + location.yaw + ":" + location.pitch
    }

    fun deserialize(source: String?): Location? {
        if (source == null || source.equals("null", ignoreCase = true)) {
            return null
        }
        val split = source.split(":".toRegex()).toTypedArray()
        val world = Bukkit.getServer().getWorld(split[0]) ?: return null
        return Location(
            world,
            split[1].toDouble(),
            split[2].toDouble(),
            split[3].toDouble(),
            split[4].toFloat(),
            split[5].toFloat()
        )
    }
}