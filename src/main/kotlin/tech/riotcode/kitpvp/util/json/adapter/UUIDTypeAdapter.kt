package tech.riotcode.kitpvp.util.json.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import tech.riotcode.kitpvp.util.json.adapter.abstraction.CustomTypeAdapter
import java.io.IOException
import java.util.*

class UUIDTypeAdapter : CustomTypeAdapter<UUID>(UUID::class.java) {

    @Throws(IOException::class)
    override fun write(jsonWriter: JsonWriter, uuid: UUID?) {
        jsonWriter.value(uuid?.toString())
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): UUID? {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull()
            return null
        }
        return UUID.fromString(jsonReader.nextString())
    }
}