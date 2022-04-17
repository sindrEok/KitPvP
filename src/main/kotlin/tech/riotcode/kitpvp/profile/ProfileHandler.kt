package tech.riotcode.kitpvp.profile

import org.bson.Document
import org.bukkit.Bukkit
import tech.riotcode.kitpvp.KitpvpPlugin.Companion.plugin
import tech.riotcode.kitpvp.database.MongoHandler
import tech.riotcode.kitpvp.profile.exception.UnloadedProfileException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

object ProfileHandler {
    val profileMap = ConcurrentHashMap<UUID, Profile>()

    fun findProfile(uuid: UUID) = profileMap[uuid] ?: throw UnloadedProfileException(Bukkit.getPlayer(uuid))

    var globalCoinMultiplier: Float = 1F

    fun close() {
        saveProfiles(false)
    }

    fun saveProfiles(async: Boolean) {
        profileMap.values.forEach {
            saveProfile(it, async)
        }
    }

    fun loadProfile(profile: Profile, async: Boolean) {
        val consumer: Consumer<Document?> = Consumer { document ->
            if (document != null) {
                val fromJson = plugin.jsonHelper.gson.fromJson(document.toJson(), Profile::class.java)
                fromJson.player = profile.player
                profileMap[profile.uuid] = fromJson

            }
            profileMap[profile.uuid]!!.loadedData = true
        }
        MongoHandler.documentLoad(consumer, "profile", "uuid", profile.uuid.toString(), async)
    }

    private fun saveProfile(profile: Profile, async: Boolean) {
        val jsonProfile = plugin.jsonHelper.gson.toJson(profile)
        MongoHandler.documentSave(
            collection = "profile",
            json = jsonProfile,
            key = "uuid",
            value = profile.uuid.toString(),
            async = async
        )
    }
}