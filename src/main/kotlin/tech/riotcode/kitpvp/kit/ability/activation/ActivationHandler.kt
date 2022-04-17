package tech.riotcode.kitpvp.kit.ability.activation

import tech.riotcode.kitpvp.util.TaskScheduler
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ActivationHandler {

    private val playerMap: ConcurrentHashMap<UUID, Float> = ConcurrentHashMap()

    init {
        TaskScheduler.runAsyncRepeating({
            tick()
        }, 0, 4)
    }

    fun isActive(uuid: UUID): Boolean = playerMap.containsKey(uuid)

    fun addPlayer(uuid: UUID, time: Float) = playerMap.put(uuid, time)

    fun removePlayer(uuid: UUID) = playerMap.remove(uuid)

    private fun tick() {
        playerMap.forEach { (uuid, time) ->
            if (time <= 0) {
                playerMap.remove(uuid)
            } else {
                playerMap[uuid] = time - 0.20F
            }
        }
    }
}