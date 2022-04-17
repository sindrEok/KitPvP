package tech.riotcode.kitpvp.util

import com.google.common.base.Joiner
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import java.util.*

/*
 * Author: Idk, got this from curtis a long time ago
 */

object ItemSerializer {
    val HELMET_MATERIALS = listOf(
        Material.DIAMOND_HELMET,
        Material.IRON_HELMET,
        Material.CHAINMAIL_HELMET,
        Material.GOLD_HELMET,
        Material.LEATHER_HELMET
    )
    val CHESTPLATE_MATERIALS = Arrays.asList(
        Material.DIAMOND_CHESTPLATE,
        Material.IRON_CHESTPLATE,
        Material.CHAINMAIL_CHESTPLATE,
        Material.GOLD_CHESTPLATE,
        Material.LEATHER_CHESTPLATE
    )
    val LEGGINS_MATERIALS = Arrays.asList(
        Material.DIAMOND_LEGGINGS,
        Material.IRON_LEGGINGS,
        Material.CHAINMAIL_LEGGINGS,
        Material.GOLD_LEGGINGS,
        Material.LEATHER_LEGGINGS
    )
    val BOOTS_MATERIALS = Arrays.asList(
        Material.DIAMOND_BOOTS,
        Material.IRON_BOOTS,
        Material.CHAINMAIL_BOOTS,
        Material.GOLD_BOOTS,
        Material.LEATHER_BOOTS
    )

    fun fixInventoryOrder(source: Array<ItemStack?>?): Array<ItemStack?> {
        val fixed = arrayOfNulls<ItemStack>(36)
        System.arraycopy(source, 0, fixed, 27, 9)
        System.arraycopy(source, 9, fixed, 0, 27)
        return fixed
    }

    fun serializeInventory(source: Array<ItemStack?>): String {
        val builder = StringBuilder()
        for (itemStack in source) {
            builder.append(serializeItemStack(itemStack))
            builder.append(";")
        }
        return builder.toString()
    }

    fun deserializeInventory(source: String): Array<ItemStack?> {
        val items: MutableList<ItemStack?> = ArrayList()
        val split = source.split(";".toRegex()).toTypedArray()
        for (piece in split) {
            items.add(deserializeItemStack(piece))
        }
        return items.toTypedArray()
    }

    fun serializeItemStack(item: ItemStack?): String {
        val builder = StringBuilder()
        if (item == null) {
            return "null"
        }
        val isType = item.type.id.toString()
        builder.append("t@").append(isType)
        if (item.durability.toInt() != 0) {
            val isDurability = item.durability.toString()
            builder.append(":d@").append(isDurability)
        }
        if (item.amount != 1) {
            val isAmount = item.amount.toString()
            builder.append(":a@").append(isAmount)
        }
        if (item.hasItemMeta()) {
            val itemMeta = item.itemMeta
            if (itemMeta.hasDisplayName()) {
                builder.append(":dn@").append(itemMeta.displayName)
            }
            if (itemMeta.hasLore()) {
                builder.append(":l@").append("[").append(Joiner.on(",,").join(itemMeta.lore)).append("]")
            }
            if (itemMeta.hasEnchants()) {
                val enchantments = item.itemMeta.enchants
                enchantments.forEach { key, value ->
                    builder.append(":e@").append(key.id).append("@").append(value)
                }
            }
        }
        if (item.type == Material.POTION) {
            val potion = Potion.fromItemStack(item)
            builder.append(":pd@")
                .append(potion.type.damageValue)
                .append("-")
                .append(potion.level)
            for (effect in potion.effects) {
                builder.append("=")
                    .append(effect.type.id)
                    .append("-")
                    .append(effect.duration)
                    .append("-")
                    .append(effect.amplifier)
            }
        }
        if (item.type == Material.LEATHER_HELMET ||
            item.type == Material.LEATHER_CHESTPLATE ||
            item.type == Material.LEATHER_LEGGINGS || item.type == Material.LEATHER_BOOTS &&
            item.hasItemMeta()
        ) {
            val meta = item.itemMeta as LeatherArmorMeta
            builder.append(":lc@").append(meta.color.asRGB())
        }
        if (item.type == Material.BOOK_AND_QUILL || item.type == Material.WRITTEN_BOOK && item.hasItemMeta()) {
            val meta = item.itemMeta as BookMeta
            if (meta.hasAuthor()) {
                builder.append(":ba@").append(meta.author)
            }
            if (meta.hasTitle()) {
                builder.append(":bt@").append(meta.title)
            }
            if (meta.hasPages()) {
                builder.append(":bp@").append("[").append(Joiner.on(";;").join(meta.pages)).append("]")
            }
        }
        if (item.type == Material.ENCHANTED_BOOK && item.hasItemMeta()) {
            val meta = item.itemMeta as EnchantmentStorageMeta
            if (meta.hasStoredEnchants()) {
                meta.storedEnchants.forEach { key, value ->
                    builder.append(":esm@").append(key.id).append("@").append(value)
                }
            }
        }
        return builder.toString()
    }

    fun deserializeItemStack(`in`: String): ItemStack? {
        var item: ItemStack? = null
        var meta: ItemMeta? = null
        if (`in` == "null") {
            return ItemStack(Material.AIR)
        }
        val split = `in`.split(":".toRegex()).toTypedArray()
        for (itemInfo in split) {
            val itemAttribute = itemInfo.split("@".toRegex()).toTypedArray()
            val attributeId = itemAttribute[0]
            when (attributeId) {
                "t" -> {
                    item = ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])))
                    meta = item.itemMeta
                }
                "d" -> {
                    if (item != null) {
                        item.durability = itemAttribute[1].toShort()
                        break
                    }
                }
                "a" -> {
                    if (item != null) {
                        item.amount = Integer.valueOf(itemAttribute[1])
                        break
                    }
                }
                "e" -> {
                    if (meta != null) {
                        meta.addEnchant(
                            Enchantment.getById(itemAttribute[1].toInt()), itemAttribute[2].toInt(),
                            true
                        )
                        break
                    }
                }
                "dn" -> {
                    if (meta != null) {
                        meta.displayName = itemAttribute[1]
                        break
                    }
                }
                "l" -> {
                    itemAttribute[1] = itemAttribute[1].replace("[", "")
                    itemAttribute[1] = itemAttribute[1].replace("]", "")
                    val lore = Arrays.asList(*itemAttribute[1].split(",,".toRegex()).toTypedArray())
                    var x = 0
                    while (x < lore.size) {
                        var s = lore[x]
                        if (s != null) {
                            if (s.toCharArray().size != 0) {
                                if (s[0] == ' ') {
                                    s = s.replaceFirst(" ".toRegex(), "")
                                }
                                lore[x] = s
                            }
                        }
                        ++x
                    }
                    if (meta != null) {
                        meta.lore = lore
                        break
                    }
                }
                "pd" -> {
                    if (item != null && item.type == Material.POTION) {
                        val effectsList = itemAttribute[1].split("=".toRegex()).toTypedArray()
                        val potionData = effectsList[0].split("-".toRegex()).toTypedArray()
                        val potion = Potion(PotionType.getByDamageValue(potionData[0].toInt()), potionData[1].toInt())
                        potion.isSplash = item.durability >= 16000
                        val potionMeta = item.itemMeta as PotionMeta
                        var i = 1
                        while (i < effectsList.size) {
                            val effectData = effectsList[1].split("-".toRegex()).toTypedArray()
                            val potionEffect = PotionEffect(
                                PotionEffectType.getById(
                                    Integer.valueOf(effectData[0])
                                ), java.lang.Double.valueOf(effectData[1]).toInt(),
                                Integer.valueOf(effectData[2]), false
                            )
                            potionMeta.addCustomEffect(potionEffect, true)
                            i++
                        }
                        item = potion.toItemStack(item.amount)
                        item.itemMeta = potionMeta
                    }
                }
                "lc" -> {
                    if (meta != null) {
                        val armorMeta = item!!.itemMeta as LeatherArmorMeta
                        armorMeta.color = Color.fromRGB(Integer.valueOf(itemAttribute[1]))
                        item.itemMeta = armorMeta
                    }
                }
                "ba" -> {
                    if (meta != null) {
                        val bookMeta = item!!.itemMeta as BookMeta
                        bookMeta.author = itemAttribute[1]
                        item.itemMeta = bookMeta
                    }
                }
                "bt" -> {
                    if (meta != null) {
                        val bookMeta = item!!.itemMeta as BookMeta
                        bookMeta.title = itemAttribute[1]
                        item.itemMeta = bookMeta
                    }
                }
                "bp" -> {
                    itemAttribute[1] = itemAttribute[1].replace("[", "")
                    itemAttribute[1] = itemAttribute[1].replace("]", "")
                    val pages = Arrays.asList(*itemAttribute[1].split(",,".toRegex()).toTypedArray())
                    var x = 0
                    while (x < pages.size) {
                        var s = pages[x]
                        if (s != null) {
                            if (s.toCharArray().size != 0) {
                                if (s[0] == ' ') {
                                    s = s.replaceFirst(" ".toRegex(), "")
                                }
                                pages[x] = s
                            }
                        }
                        ++x
                    }
                    if (meta != null) {
                        val bookMeta = item!!.itemMeta as BookMeta
                        bookMeta.pages = pages
                        item.itemMeta = bookMeta
                        break
                    }
                }
                "esm" -> {
                    if (meta != null) {
                        val storageMeta = item!!.itemMeta as EnchantmentStorageMeta
                        storageMeta.addStoredEnchant(
                            Enchantment.getById(Integer.valueOf(itemAttribute[1])),
                            Integer.valueOf(itemAttribute[2]),
                            true
                        )
                        item.itemMeta = storageMeta
                        break
                    }
                }
            }
        }
        if (meta != null && (meta.hasDisplayName() || meta.hasLore())) {
            item!!.itemMeta = meta
        }
        return item
    }

    fun removeCrafting(material: Material?) {
        //Iterator<Recipe> iterator = ILib.getInstance().getServer().recipeIterator();

        /*while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (recipe != null && recipe.getResult().getType() == material) {
                iterator.remove();
            }
        }*/
    }

    fun isHelmet(material: Material): Boolean {
        return HELMET_MATERIALS.contains(material)
    }

    fun isHelmet(itemStack: ItemStack): Boolean {
        return isHelmet(itemStack.type)
    }

    fun isChestplate(material: Material): Boolean {
        return CHESTPLATE_MATERIALS.contains(material)
    }

    fun isChestplate(itemStack: ItemStack): Boolean {
        return isChestplate(itemStack.type)
    }

    fun isLeggings(material: Material): Boolean {
        return LEGGINS_MATERIALS.contains(material)
    }

    fun isLeggings(itemStack: ItemStack): Boolean {
        return isLeggings(itemStack.type)
    }

    fun isBoots(material: Material): Boolean {
        return BOOTS_MATERIALS.contains(material)
    }

    fun isBoots(itemStack: ItemStack): Boolean {
        return isBoots(itemStack.type)
    }
}