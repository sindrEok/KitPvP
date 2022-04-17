package tech.riotcode.kitpvp.kit.impl

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.kit.Kit
import tech.riotcode.kitpvp.kit.ability.impl.SpikyAbility
import tech.riotcode.kitpvp.util.ItemBuilder

class HedgehogKit : Kit(SpikyAbility()) {

    override var inventory: Array<ItemStack?> = arrayOfNulls<ItemStack>(35).apply {
        this[0] = ItemBuilder(Material.IRON_SWORD).setUnbreakable(true).build()
        this[ability.slot] = ability.item
        this.fill(ItemStack(Material.MUSHROOM_SOUP), 2, 34)
    }

    override var armor: Array<ItemStack> = arrayOf(
        ItemStack(Material.IRON_HELMET),
        ItemStack(Material.IRON_CHESTPLATE),
        ItemStack(Material.IRON_LEGGINGS),
        ItemStack(Material.IRON_BOOTS)
    ).reversedArray()

    override val name: String = "Hedgehog"

    override var icon: ItemStack = ItemBuilder(Material.IRON_FENCE).name(name).build()
}
