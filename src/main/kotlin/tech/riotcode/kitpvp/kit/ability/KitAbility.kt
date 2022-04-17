package tech.riotcode.kitpvp.kit.ability

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface KitAbility {

    fun onAbility(player: Player, obj: Any?)

    var activationTime: Float

    val abilityType: AbilityType
    var item: ItemStack
    var slot: Int
    var cooldown: Float

}