package tech.riotcode.kitpvp.profile.killstreak

import java.util.*

object KillstreakHandler {

    private val killstreakMap = mutableMapOf<UUID, Int>()

    fun findKillstreak(uuid: UUID) = killstreakMap[uuid]

    fun addKillstreak(uuid: UUID) =
        if (findKillstreak(uuid) == null) killstreakMap.put(uuid, 1)
        else killstreakMap.replace(uuid, findKillstreak(uuid)!! + 1)

    fun clearKillstreak(uuid: UUID) = killstreakMap.remove(uuid)
}
