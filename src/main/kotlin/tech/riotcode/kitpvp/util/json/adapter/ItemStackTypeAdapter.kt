package tech.riotcode.kitpvp.util.json.adapter

import com.google.gson.JsonParseException
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.util.json.adapter.abstraction.CustomTypeAdapter
import java.io.IOException

/*
 * Author: Some internet stuff, idk (SpigotMC or something)
 */

class ItemStackTypeAdapter : CustomTypeAdapter<ItemStack>(ItemStack::class.java) {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: ItemStack?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.beginObject()
        out.name(MATERIAL)
        out.value(value.type.name)
        val durability = value.durability
        if (durability.toInt() != 0) {
            out.name(DATA_VALUE)
            out.value(durability.toLong())
        }
        val amount = value.amount
        if (amount != 1) {
            out.name(AMOUNT)
            out.value(amount.toLong())
        }
        val itemMeta = value.itemMeta
        if (itemMeta != null) {
            val lore = itemMeta.lore
            if (lore != null) {
                out.name(LORE)
                out.beginArray()
                for (s in lore) out.value(s)
                out.endArray()
            }
            val name = itemMeta.displayName
            if (name != null) {
                out.name(NAME)
                out.value(name)
            }
        }
        val enchantments = value.enchantments
        if (enchantments != null && enchantments.isNotEmpty()) {
            out.name(ENCHANTS)
            out.beginObject()
            for ((key, value1) in enchantments) {
                out.name(key.name)
                out.value(value1)
            }
            out.endObject()
        }
        out.endObject()
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): ItemStack? {
        var material: Material? = null
        var amount: Int? = null
        var dataValue: Short? = null
        var name: String? = null
        val lore: MutableList<String> = ArrayList()
        val enchantments: MutableMap<Enchantment, Int> = HashMap()
        if (`in`.peek() == JsonToken.NULL) {
            `in`.skipValue()
            return null
        }
        `in`.beginObject()
        while (`in`.hasNext()) when (`in`.nextName()) {
            MATERIAL -> material = Material.getMaterial(`in`.nextString())
            DATA_VALUE -> dataValue = `in`.nextInt().toShort()
            AMOUNT -> amount = `in`.nextInt()
            LORE -> {
                `in`.beginArray()
                while (`in`.hasNext()) {
                    lore.add(`in`.nextString())
                }
                `in`.endArray()
            }
            NAME -> name = `in`.nextString()
            ENCHANTS -> {
                `in`.beginObject()
                while (`in`.hasNext()) enchantments[Enchantment.getByName(`in`.nextName())] = `in`.nextInt()
                `in`.endObject()
            }
        }
        if (material == null) throw JsonParseException("An ItemStack must have a material field!")
        val stack = ItemStack(material)
        if (amount != null) stack.amount = amount
        if (dataValue != null) stack.durability = dataValue
        if (name != null || lore.isNotEmpty()) {
            val itemMeta = stack.itemMeta
            if (name != null) itemMeta.displayName = name
            if (lore.isNotEmpty()) itemMeta.lore = lore
            stack.itemMeta = itemMeta
        }
        if (enchantments.isNotEmpty()) for ((key, value) in enchantments) {
            stack.addUnsafeEnchantment(key, value)
        }
        `in`.endObject()
        return stack
    }

    companion object {
        private const val MATERIAL = "m"
        private const val DATA_VALUE = "d"
        private const val AMOUNT = "a"
        private const val LORE = "l"
        private const val NAME = "n"
        private const val ENCHANTS = "e"
    }
}