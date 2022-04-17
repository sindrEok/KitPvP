package tech.riotcode.kitpvp.event.countdown

import org.bukkit.entity.Player
import tech.riotcode.kitpvp.KitpvpPlugin.Companion.plugin
import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.countdown.task.CountdownTask

object CountdownHandler {

    val isActive: Boolean get() = currentTask.active

    var currentTask: CountdownTask = CountdownTask()

    fun startCountdown(seconds: Int, interval: Array<Int>, eventEnum: EventEnum, player: Player) {
        currentTask.apply {
            this.seconds = seconds
            this.interval = interval
            this.eventEnum = eventEnum
            this.player = player
        }.runTaskTimer(plugin, 0, 20)
    }

    fun stopCountdown() {
        currentTask.apply {
            active = false
            cancel()
        }
    }
}