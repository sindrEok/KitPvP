package tech.riotcode.kitpvp.event

import org.bson.Document
import tech.riotcode.kitpvp.KitpvpPlugin.Companion.plugin
import tech.riotcode.kitpvp.database.MongoHandler
import tech.riotcode.kitpvp.event.arena.Arena
import tech.riotcode.kitpvp.event.impl.sumo.SumoEvent
import java.util.function.Consumer

object EventHandler {

    private val eventMap = hashMapOf<String, EventType>(
        "SUMO" to SumoEvent(plugin, this),
    )

    val arenaMap: MutableMap<String, Arena> = HashMap()

    var activeEvent: EventType? = null

    fun findEvent(enum: EventEnum): EventType =
        eventMap[enum.name.uppercase()] ?: throw Exception("Coding error, sindre!")

    init {
        loadArenas()
    }

    fun close() {
        saveArenas(false)
    }

    fun saveArenas(async: Boolean) {
        arenaMap.forEach { (name, arena) ->
            MongoHandler.documentSave(
                "arenas",
                plugin.jsonHelper.gson.toJson(arena),
                "name",
                name,
                async
            )
        }
    }

    private fun loadArenas() {
        val consumer: Consumer<List<Document?>> = Consumer { documents ->
            documents.forEach {
                if (it != null) {
                    val arena: Arena = plugin.jsonHelper.gson.fromJson(it.toJson(), Arena::class.java)
                    arenaMap[arena.name] = arena
                }
            }
        }

        MongoHandler.documentLoadAll(consumer, "arenas", false)
    }
}