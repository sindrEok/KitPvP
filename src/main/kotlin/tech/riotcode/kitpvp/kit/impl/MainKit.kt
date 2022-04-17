package tech.riotcode.kitpvp.kit.impl

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.kit.Kit
import tech.riotcode.kitpvp.kit.ability.impl.MainAbility
import tech.riotcode.kitpvp.util.ItemBuilder

class MainKit : Kit(MainAbility()) {

    override var inventory: Array<ItemStack?> = arrayOfNulls<ItemStack?>(35).apply {
        this[0] = ItemBuilder(Material.DIAMOND_SWORD).setUnbreakable(true).build()
        this[ability.slot] = ability.item
        this.fill(ItemStack(Material.MUSHROOM_SOUP), 2, 34)
    }

    override var armor: Array<ItemStack> = arrayOf(
        ItemStack(Material.IRON_HELMET),
        ItemStack(Material.IRON_CHESTPLATE),
        ItemStack(Material.IRON_LEGGINGS),
        ItemStack(Material.IRON_BOOTS)
    ).reversedArray()

    override val name: String = "Main"

    override var icon: ItemStack = ItemBuilder(Material.DIAMOND_SWORD).name(name).build()
}