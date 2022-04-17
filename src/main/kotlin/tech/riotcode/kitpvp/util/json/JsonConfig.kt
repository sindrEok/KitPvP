package tech.riotcode.kitpvp.util.json

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import tech.riotcode.kitpvp.KitpvpPlugin.Companion.plugin
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/*
 * Author: Bridge
 */

class JsonConfig(path: File? = null, fileName: String) {

    val file = if (path != null) File(path, if (fileName.endsWith(".json")) fileName else "$fileName.json") else File(
        plugin.dataFolder,
        if (fileName.endsWith(
                ".json"
            )
        ) fileName else "$fileName.json"
    )
    val map: HashMap<String, JsonObject>

    init {
        if (!file.exists())
            Files.write(
                file.toPath(),
                JsonObjectBuilder().build().toString().toByteArray(Charsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
            )

        // thanks to kotlin we now have to specify the TypeToken or else we'll have issues
        map = plugin.jsonHelper.gson.fromJson(
            FileReader(file),
            object : TypeToken<HashMap<String, JsonObject>>() {}.type
        ) as HashMap<String, JsonObject>
    }

    fun save() {
        val jsonString = if (map.isEmpty()) "{}" else plugin.jsonHelper.gson.toJson(HashMap(map))
        val fileWriter = FileWriter(file)

        fileWriter.write(jsonString)
        fileWriter.close()
    }

    fun get(path: String): Any? {
        synchronized(map) {
            return map[path]
        }
    }

    fun getString(path: String): String {
        return get(path) as String
    }

    fun getInt(path: String): Int {
        return get(path) as Int
    }

    fun getBoolean(path: String): Boolean {
        return get(path) as Boolean
    }
}