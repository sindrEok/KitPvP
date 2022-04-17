package tech.riotcode.kitpvp.lobby

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.KitpvpPlugin.Companion.plugin
import tech.riotcode.kitpvp.profile.Profile
import tech.riotcode.kitpvp.util.Cuboid
import tech.riotcode.kitpvp.util.ItemBuilder
import tech.riotcode.kitpvp.util.LocationSerializer

object LobbyHandler {

    var lobbyLocation: Location =
        LocationSerializer.deserialize(plugin.lobbyConfig.getString("lobbyLocation"))
            ?: Bukkit.getWorlds()[0].spawnLocation

    val lobbyItems = arrayOfNulls<ItemStack?>(9).apply {
        this[0] = ItemBuilder(Material.CHEST).name("Kits").build()
    }

    var lobbyCuboid: Cuboid = Cuboid(
        plugin.lobbyConfig.getInt("lobbyCuboid.minX"),
        plugin.lobbyConfig.getInt("lobbyCuboid.maxX"),
        plugin.lobbyConfig.getInt("lobbyCuboid.minZ"),
        plugin.lobbyConfig.getInt("lobbyCuboid.maxZ")
    )

    fun close() {
        plugin.lobbyConfig.set("lobbyLocation", LocationSerializer.serialize(lobbyLocation))

        plugin.lobbyConfig.set("lobbyCuboid.minX", lobbyCuboid.minX)
        plugin.lobbyConfig.set("lobbyCuboid.maxX", lobbyCuboid.maxX)
        plugin.lobbyConfig.set("lobbyCuboid.minZ", lobbyCuboid.minZ)
        plugin.lobbyConfig.set("lobbyCuboid.maxZ", lobbyCuboid.maxZ)
    }

    fun lobbyPlayer(profile: Profile) {
        val player = profile.player.get() ?: throw Exception("wtf")
        player.teleport(lobbyLocation)
        player.inventory.clear()
        player.inventory.armorContents = null // maybe not necessary idk

        lobbyItems.forEachIndexed { index, itemStack ->
            player.inventory.setItem(index, itemStack)
        }
    }
}