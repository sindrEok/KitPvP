package tech.riotcode.kitpvp.kit.ability.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import tech.riotcode.kitpvp.kit.ability.AbilityType
import tech.riotcode.kitpvp.kit.ability.KitAbility
import tech.riotcode.kitpvp.util.ItemBuilder

class BlastAbility : KitAbility {

    @Suppress("unchecked")
    override fun onAbility(player: Player, obj: Any?) {
        val players: Array<Player> = obj as Array<Player>
        players.forEach { arrayPlayer ->
            arrayPlayer.let {
                val xLoc = arrayPlayer.location.x - player.location.x
                val zLoc = arrayPlayer.location.z - player.location.z

                it.velocity = it.velocity.add(
                    Vector(
                        if (xLoc >= 0) 1 else -1,
                        1,
                        if (zLoc >= 0) 1 else -1
                    )
                )
                it.damage(8.0)
            }
        }
    }

    override var activationTime: Float = 4F

    override val abilityType: AbilityType = AbilityType.ITEM_ABILITY

    override var item: ItemStack = ItemBuilder(Material.BLAZE_POWDER).build()

    override var slot: Int = 1

    override var cooldown: Float = 1F
}