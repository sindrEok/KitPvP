package tech.riotcode.kitpvp.kit

import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.kit.ability.KitAbility
import tech.riotcode.kitpvp.profile.Profile

abstract class Kit(val ability: KitAbility) {

    abstract var inventory: Array<ItemStack?>

    abstract var armor: Array<ItemStack>

    abstract var icon: ItemStack

    abstract val name: String

    fun apply(profile: Profile) = KitHandler.applyKit(this, profile)

    override fun toString(): String = name
}