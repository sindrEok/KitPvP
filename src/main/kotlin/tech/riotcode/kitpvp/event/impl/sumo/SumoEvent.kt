package tech.riotcode.kitpvp.event.impl.sumo

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tech.riotcode.kitpvp.KitpvpPlugin
import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.EventHandler
import tech.riotcode.kitpvp.event.EventType
import tech.riotcode.kitpvp.event.arena.Arena
import tech.riotcode.kitpvp.event.countdown.CountdownHandler
import tech.riotcode.kitpvp.event.impl.sumo.task.DeathTask
import tech.riotcode.kitpvp.event.impl.sumo.task.StartTimerTask
import tech.riotcode.kitpvp.event.sticky.StickyHandler
import tech.riotcode.kitpvp.extensions.player
import tech.riotcode.kitpvp.extensions.profile
import tech.riotcode.kitpvp.lobby.LobbyHandler
import tech.riotcode.kitpvp.util.CC
import tech.riotcode.kitpvp.util.Messages
import java.util.*
import kotlin.random.Random

class SumoEvent(
    private val plugin: KitpvpPlugin,
    val eventHandler: EventHandler
) : EventType() {

    override var hasStarted: Boolean = false

    override val eventEnum: EventEnum = EventEnum.SUMO

    override val eventItems: Map<ItemStack, Int> = emptyMap() // EMPTY

    override val players: MutableList<UUID> = ArrayList()

    val fightingPlayers: MutableList<UUID> = ArrayList()

    override lateinit var currentArena: Arena

    private lateinit var deathTask: DeathTask

    override fun host(player: Player) {
        CountdownHandler.startCountdown(60, arrayOf(60, 30, 10, 5, 3, 2, 1), eventEnum, player)
        EventHandler.activeEvent = this
    }

    override fun start() {
        deathTask = DeathTask(this)
        deathTask.runTaskTimer(plugin, 0, 1)

        hasStarted = true

        CountdownHandler.stopCountdown()
        startFight()
    }

    override fun stop() {
        EventHandler.activeEvent = null
        deathTask.cancel()
    }

    private fun startFight() {
        val startTimerTask = StartTimerTask()
        getRandomPlayers().forEachIndexed { index, uuid ->
            val player: Player = uuid.player

            val location: Location = if (index == 0) currentArena.spawnLocation1 else currentArena.spawnLocation2

            player.teleport(location)
            fightingPlayers.add(uuid)
            StickyHandler.addPlayer(player, location, 3)
            player.inventory.clear()
            deathTask.avoidException = false
            startTimerTask.addPlayer(player)
        }
        players.map { Bukkit.getPlayer(it) }.forEach {
            it.sendMessage(
                CC.of(
                    "&7Match between &3${Bukkit.getPlayer(fightingPlayers[0]).displayName}&7"
                            + " vs &3${Bukkit.getPlayer(fightingPlayers[1]).displayName}&7 has started!"
                )
            )
        }
        startTimerTask.runTaskTimer(plugin, 0, 20)
    }

    fun killPlayer(uuid: UUID) {
        val player = uuid.player
        LobbyHandler.lobbyPlayer(player.profile)

        fightingPlayers.remove(uuid)
        players.remove(uuid)

        Bukkit.getPlayer(fightingPlayers.removeAt(0)).teleport(currentArena.lobbyLocation)

        checkWinner()
    }

    private fun checkWinner() {
        if (players.size == 1) {
            val player = Bukkit.getPlayer(players.removeAt(0))
            player.teleport(Bukkit.getWorlds()[0].spawnLocation)
            Bukkit.broadcastMessage(Messages.EventWinMessage.formattedMessage.replace("%player%", player.displayName))
            stop()
        } else startFight()
    }

    private fun getRandomPlayers(): Array<UUID> {
        val cloneList = players.toMutableList()
        return arrayOf(
            cloneList.removeAt(Random.nextInt(0, cloneList.size)),
            cloneList.removeAt(Random.nextInt(0, cloneList.size))
        )
    }

    fun createArena(name: String, eventEnum: EventEnum) =
        eventHandler.arenaMap.putIfAbsent(
            name,
            Arena(
                name,
                Location(Bukkit.getWorlds()[0], 0.0, 0.0, 0.0),
                Location(Bukkit.getWorlds()[0], 0.0, 0.0, 0.0),
                Location(Bukkit.getWorlds()[0], 0.0, 0.0, 0.0),
                ItemStack(Material.DIRT),
                eventEnum
            )
        )
}