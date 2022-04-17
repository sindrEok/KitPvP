package tech.riotcode.kitpvp.kit.ability.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.kit.KitHandler
import tech.riotcode.kitpvp.kit.ability.AbilityType
import tech.riotcode.kitpvp.kit.ability.KitAbility
import tech.riotcode.kitpvp.util.ItemBuilder

class SpikyAbility : KitAbility {

    override fun onAbility(player: Player, obj: Any?) {
        KitHandler.activationHandler.addPlayer(player.uniqueId, 5F)
        player.sendMessage("You activated the spiky ability")
    }

    override var activationTime: Float = 5F

    override val abilityType: AbilityType = AbilityType.ITEM_ABILITY

    override var item: ItemStack = ItemBuilder(Material.IRON_FENCE).build()

    override var slot: Int = 1

    override var cooldown: Float = 30F
}