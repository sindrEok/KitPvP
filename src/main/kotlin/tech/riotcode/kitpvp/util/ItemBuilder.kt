package tech.riotcode.kitpvp.util

import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.material.MaterialData
import java.util.*
import java.util.function.Consumer


/*
 * Author: Bridge
 *
 * With simple modifications by sindre
 */
class ItemBuilder(itemStack: ItemStack) {

    private val item: ItemStack = itemStack

    constructor(material: Material) : this(ItemStack(material))

    fun amount(amount: Int): ItemBuilder {
        item.amount = amount
        return this
    }

    fun name(name: String): ItemBuilder {
        val meta = item.itemMeta
        meta.displayName = name.c()
        item.itemMeta = meta
        return this
    }

    fun lore(text: String): ItemBuilder {
        val meta = item.itemMeta
        var lore: MutableList<String>? = meta.lore
        if (lore == null) {
            lore = ArrayList()
        }
        lore.add(ChatColor.translateAlternateColorCodes('&', text))
        meta.lore = lore.c()
        item.itemMeta = meta
        return this
    }

    fun lore(vararg text: String): ItemBuilder {
        Arrays.stream(text).forEach { this.lore(it) }
        return this
    }

    fun lore(text: List<String>): ItemBuilder {
        text.forEach { lore(it) }
        return this
    }

    fun setUnbreakable(unbreakable: Boolean): ItemBuilder {
        item.itemMeta.spigot().isUnbreakable = true
        return this
    }

    fun durability(durability: Int): ItemBuilder {
        item.durability = durability.toShort()
        return this
    }

    fun data(data: Int): ItemBuilder {
        item.data = MaterialData(item.type, data.toByte())
        return this
    }

    fun enchantment(enchantment: Enchantment, level: Int): ItemBuilder {
        item.addUnsafeEnchantment(enchantment, level)
        return this
    }

    fun enchantment(enchantment: Enchantment): ItemBuilder {
        item.addUnsafeEnchantment(enchantment, 1)
        return this
    }

    fun type(material: Material): ItemBuilder {
        item.type = material
        return this
    }

    fun clearLore(): ItemBuilder {
        val meta = item.itemMeta
        meta.lore = ArrayList()
        item.itemMeta = meta
        return this
    }

    fun clearEnchantments(): ItemBuilder {
        item.enchantments.keys.forEach(Consumer<Enchantment> { item.removeEnchantment(it) })
        return this
    }

    fun color(color: Color): ItemBuilder {
        if (item.type == Material.LEATHER_BOOTS
            || item.type == Material.LEATHER_CHESTPLATE
            || item.type == Material.LEATHER_HELMET
            || item.type == Material.LEATHER_LEGGINGS
        ) {

            val meta = item.itemMeta as LeatherArmorMeta
            meta.color = color
            item.itemMeta = meta
            return this
        } else {
            throw IllegalArgumentException("Colors only applicable for leather armor!")
        }
    }

    fun flag(vararg flag: ItemFlag): ItemBuilder {
        val meta = item.itemMeta
        meta.addItemFlags(*flag)
        item.itemMeta = meta
        return this
    }

    fun build(): ItemStack {
        return item
    }

    private fun String.c(): String {
        return ChatColor.translateAlternateColorCodes('&', this)
    }

    private fun List<String>.c(): List<String> {
        val tempStringList = ArrayList<String>()
        for (text in this) {
            tempStringList.add(text.c())
        }
        return tempStringList
    }
}