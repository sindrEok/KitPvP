package tech.riotcode.kitpvp.kit.ability.listener

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import tech.riotcode.kitpvp.extensions.profile
import tech.riotcode.kitpvp.kit.KitHandler
import tech.riotcode.kitpvp.kit.ability.AbilityType
import tech.riotcode.kitpvp.profile.Profile
import tech.riotcode.kitpvp.util.Messages

object AbilityListener : Listener {

    @EventHandler
    fun onAbility(event: PlayerInteractEvent) {
        val profile: Profile = event.player.profile

        profile.currentKit?.let {
            if (event.item == null || it.ability.abilityType != AbilityType.ITEM_ABILITY || !profile.inWarzone) {
                return
            }
            if (event.item.isSimilar(it.ability.item)) {
                val cooldown = KitHandler.kitCooldown.getCooldown(profile.uuid)

                if (cooldown == null) {
                    when (it.name) {
                        "Dragon" -> {
                            val array = event.player.getNearbyEntities(4.0, 4.0, 4.0).filter { entity ->
                                entity.type == EntityType.PLAYER
                            }.map { entity -> entity as Player }.toTypedArray()

                            it.ability.onAbility(event.player, array)
                        }
                        else -> it.ability.onAbility(event.player, null)
                    }
                    KitHandler.kitCooldown.cooldownMap[profile.uuid] = it.ability.cooldown
                } else {
                    val toInt = cooldown.toInt()

                    event.player.sendMessage(
                        Messages.CooldownAbilityMessage.formattedMessage
                            .replace("%cooldown%", toInt.toString())
                            .replace("(s)", if (toInt == 1) "" else "s")
                    )
                }
            }
        }
    }


    @EventHandler
    fun onPlayerDrop(event: PlayerDropItemEvent) {
        val profile = event.player.profile

        event.isCancelled =
            (profile.currentKit != null && event.itemDrop.itemStack.isSimilar(profile.currentKit!!.ability.item))
    }

    @EventHandler(ignoreCancelled = true)
    fun spikyAbilityEvent(event: EntityDamageByEntityEvent) {
        if (event.cause != DamageCause.ENTITY_ATTACK || event.damager !is Player) return

        if (event.entity is Player) {
            val player = event.entity as Player
            val profile = player.profile

            profile.currentKit?.let {
                if (it.toString() == "Hedgehog" && KitHandler.activationHandler.isActive(player.uniqueId)) {
                    (event.damager as Player).damage(event.finalDamage, event.entity)
                }
            }
        }
    }

    /*@EventHandler
    fun armorLessDamage(event: EntityDamageByEntityEvent) {
        if (event.cause != DamageCause.ENTITY_ATTACK || event.damager !is Player) return

        event.finalDamage

        val player = event.entity as Player
        player.damage(event.damage, event.damager)
        event.damage = 0.0
    }*/


}