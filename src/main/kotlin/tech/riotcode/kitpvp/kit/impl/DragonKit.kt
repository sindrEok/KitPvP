package tech.riotcode.kitpvp.kit.impl

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.kit.Kit
import tech.riotcode.kitpvp.kit.ability.impl.BlastAbility
import tech.riotcode.kitpvp.util.ItemBuilder

class DragonKit : Kit(BlastAbility()) {

    override var inventory: Array<ItemStack?> = arrayOfNulls<ItemStack>(35).apply {
        this[0] = ItemBuilder(Material.IRON_SWORD).setUnbreakable(true).build()
        this[ability.slot] = ability.item
        this.fill(ItemBuilder(Material.MUSHROOM_SOUP).build(), 2, 34)
    }

    override var armor: Array<ItemStack> = arrayOf(
        ItemStack(Material.IRON_HELMET),
        ItemStack(Material.IRON_CHESTPLATE),
        ItemStack(Material.IRON_LEGGINGS),
        ItemStack(Material.IRON_BOOTS)
    ).reversedArray()

    override var icon: ItemStack = ItemBuilder(Material.BLAZE_POWDER).build()

    override val name: String = "Dragon"
}