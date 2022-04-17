package tech.riotcode.kitpvp.kit.ability.impl

import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.kit.ability.AbilityType
import tech.riotcode.kitpvp.kit.ability.KitAbility
import tech.riotcode.kitpvp.util.ItemBuilder
import tech.riotcode.kitpvp.util.TaskScheduler

class DogAbility : KitAbility {

    override fun onAbility(player: Player, obj: Any?) {
        val nearbyEntities = player.getNearbyEntities(8.0, 8.0, 8.0).apply {
            this.remove(player as Entity)
        }
        val target = nearbyEntities
            .minByOrNull { it.location.distance(player.location) }

        val dogList = ArrayList<Wolf>()

        for (i in 0..5) {
            val wolf = player.world.spawnEntity(player.location, EntityType.WOLF) as Wolf
            wolf.setAdult()
            wolf.setBreed(false)
            wolf.isAngry = true
            wolf.owner = player

            target?.let {
                wolf.target = it as LivingEntity
            }

            dogList.add(wolf)
        }

        TaskScheduler.runLater(
            {
                dogList.forEach {
                    it.remove()
                }
            }, 20 * 10
        )
    }

    override var activationTime: Float = 0.0f

    override val abilityType: AbilityType = AbilityType.ITEM_ABILITY


    override var item: ItemStack = ItemBuilder(Material.BONE).build()


    override var slot: Int = 1

    override var cooldown: Float = 35f
}