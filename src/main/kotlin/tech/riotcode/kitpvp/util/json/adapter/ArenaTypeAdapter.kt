package tech.riotcode.kitpvp.util.json.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.arena.Arena
import tech.riotcode.kitpvp.util.ItemSerializer
import tech.riotcode.kitpvp.util.LocationSerializer
import tech.riotcode.kitpvp.util.json.adapter.abstraction.CustomTypeAdapter
import java.io.IOException

class ArenaTypeAdapter : CustomTypeAdapter<Arena>(Arena::class.java) {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Arena?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.beginObject()

        out.name("name")
        out.value(value.name)
        out.name("lobbyLocation")
        out.value(LocationSerializer.serialize(value.lobbyLocation))
        out.name("location1")
        out.value(LocationSerializer.serialize(value.spawnLocation1))
        out.name("location2")
        out.value(LocationSerializer.serialize(value.spawnLocation2))
        out.name("icon")
        out.value(ItemSerializer.serializeItemStack(value.icon))
        out.name("eventEnum")
        out.value(value.eventEnum.name)

        out.endObject()
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Arena? {
        if (reader.peek() == JsonToken.NULL) {
            reader.skipValue()
            return null
        }
        var name: String? = null
        var lobbyLocation: Location? = null
        var location1: Location? = null
        var location2: Location? = null
        var icon: ItemStack? = null
        var eventEnum: EventEnum? = null

        reader.beginObject()

        while (reader.hasNext()) {
            if (reader.peek() != JsonToken.NAME) {
                reader.skipValue()
            } else {
                when (reader.nextName()) {
                    "name" -> name = reader.nextString()
                    "lobbyLocation" -> lobbyLocation = LocationSerializer.deserialize(reader.nextString())
                    "location1" -> location1 = LocationSerializer.deserialize(reader.nextString())
                    "location2" -> location2 = LocationSerializer.deserialize(reader.nextString())
                    "icon" -> icon = ItemSerializer.deserializeItemStack(reader.nextString())
                    "eventEnum" -> eventEnum = EventEnum.valueOf(reader.nextString())
                }
            }
        }
        reader.endObject()
        return Arena(name!!, lobbyLocation!!, location1!!, location2!!, icon!!, eventEnum!!)
    }
}