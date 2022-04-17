package tech.riotcode.kitpvp.lobby.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.extensions.profile
import tech.riotcode.kitpvp.kit.KitHandler
import tech.riotcode.kitpvp.kit.menu.SelectKitMenu
import tech.riotcode.kitpvp.lobby.LobbyHandler
import tech.riotcode.kitpvp.util.TaskScheduler


object LobbyListener : Listener {

    @EventHandler
    fun onFood(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onMob(event: CreatureSpawnEvent) {
        if (event.spawnReason != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun cancelSwordDrop(event: PlayerDropItemEvent) {
        if (!event.isCancelled)
            event.isCancelled = event.itemDrop.itemStack.type.name.endsWith("_SWORD")
    }

    @EventHandler
    fun itemDrop(event: ItemSpawnEvent) {
        val itemName = event.entity.itemStack.type.name

        if (event.entity.itemStack.type == Material.BOWL) {
            event.isCancelled = true
        } else {
            event.isCancelled = itemName.endsWith("_HELMET")
                    || itemName.endsWith("_LEGGINGS")
                    || itemName.endsWith("_BOOTS")
                    || itemName.endsWith("_CHESTPLATE")
                    || itemName.endsWith("_SWORD")
        }
        TaskScheduler.runLater(
            {
                event.entity.remove()
            }, 5 * 20
        )
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        event.drops.removeAll { itemStack -> !itemStack.isSimilar(ItemStack(Material.MUSHROOM_SOUP)) }
        event.deathMessage = ""

        TaskScheduler.runLater({
            event.entity.spigot().respawn()

            val profile = event.entity.profile

            LobbyHandler.lobbyPlayer(profile)
            KitHandler.purgePlayer(profile)
        }, 1)
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (!event.player.profile.buildMode) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlace(event: PlayerInteractEvent) {
        if (!event.player.profile.buildMode && event.action == Action.RIGHT_CLICK_BLOCK) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val item: ItemStack? = LobbyHandler.lobbyItems.firstOrNull { itemStack ->
            itemStack?.isSimilar(event.item) ?: false
        }

        item?.let {
            when (it.itemMeta.displayName) {
                "Kits" -> {
                    SelectKitMenu(event.player).open()
                }
            }
        }
    }
}