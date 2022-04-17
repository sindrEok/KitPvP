package tech.riotcode.kitpvp.util.json.adapter.abstraction

import com.google.gson.TypeAdapter

abstract class CustomTypeAdapter<T>(val typeClass: Class<T>) : TypeAdapter<T>()
