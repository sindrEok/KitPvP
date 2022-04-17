package tech.riotcode.kitpvp.util.json

import com.google.gson.JsonElement
import com.google.gson.JsonObject

class JsonObjectBuilder {
    private val jsonObject = JsonObject()

    fun append(key: String, value: String): JsonObjectBuilder {
        jsonObject.addProperty(key, value)
        return this
    }

    fun append(key: String, value: Number): JsonObjectBuilder {
        jsonObject.addProperty(key, value)
        return this
    }

    fun append(key: String, value: JsonElement?): JsonObjectBuilder {
        jsonObject.add(key, value)
        return this
    }

    fun build(): JsonObject {
        return jsonObject
    }
}

