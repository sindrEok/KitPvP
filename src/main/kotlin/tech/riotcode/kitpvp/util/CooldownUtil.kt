package tech.riotcode.kitpvp.util

import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class CooldownUtil {
    val cooldownMap: ConcurrentHashMap<UUID, Float> = ConcurrentHashMap()

    fun getCooldown(uuid: UUID): Float? {
        return cooldownMap[uuid]
    }

    fun addCooldown(uuid: UUID, float: Float) = cooldownMap.put(uuid, float)

    fun removeCooldown(uuid: UUID) = cooldownMap.remove(uuid)

    init {
        TaskScheduler.runAsyncRepeating({
            cooldownMap.forEach { (key, value) ->
                if (value <= 0F) {
                    cooldownMap.remove(key, value)
                    Bukkit.getPlayer(key).sendMessage(Messages.CooldownExpiredMessage.formattedMessage)
                } else {
                    cooldownMap[key] = value - 0.25F
                }
            }
        }, 0, 5)
    }
}