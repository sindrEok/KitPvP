package tech.riotcode.kitpvp.kit.ability.impl

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.kit.ability.AbilityType
import tech.riotcode.kitpvp.kit.ability.KitAbility
import tech.riotcode.kitpvp.util.ItemBuilder

class MainAbility : KitAbility {

    override fun onAbility(player: Player, obj: Any?) {
        Bukkit.broadcastMessage("Main Kit!")
    }

    override var activationTime: Float = 0F

    override val abilityType: AbilityType = AbilityType.ITEM_ABILITY

    override var item: ItemStack = ItemBuilder(Material.STONE_HOE).name("ability l0l!").build()
    override var slot = 1

    override var cooldown: Float = 5F
}