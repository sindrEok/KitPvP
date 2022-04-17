package tech.riotcode.kitpvp.util.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import tech.riotcode.kitpvp.util.json.adapter.abstraction.CustomTypeAdapter

class JsonHelper(vararg typeAdapters: CustomTypeAdapter<out Any>) {

    companion object {
        @JvmStatic
        val JSON_PARSER = JsonParser() // Add backwards compatibility
    }

    val gson: Gson = GsonBuilder()
        .apply {
            typeAdapters.forEach {
                registerTypeAdapter(it.typeClass, it.nullSafe())
            }
        }.setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .create()

}