package tech.riotcode.kitpvp.util

import org.bukkit.Bukkit
import tech.riotcode.kitpvp.KitpvpPlugin
import java.util.concurrent.ExecutorService

object TaskScheduler {
    fun runAsyncRepeating(runnable: Runnable, delay: Int, interval: Int) {
        Bukkit.getScheduler()
            .runTaskTimerAsynchronously(KitpvpPlugin.plugin, runnable, delay.toLong(), interval.toLong())
    }

    fun runAsyncRepeating(runnable: Runnable, delay: Int, interval: Int, executorService: ExecutorService) {
        executorService.execute {
            Bukkit.getScheduler().runTaskTimer(
                KitpvpPlugin.plugin,
                runnable,
                delay.toLong(),
                interval.toLong()
            )
        }
    }

    fun runRepeating(runnable: Runnable, delay: Int, interval: Int) {
        Bukkit.getScheduler().runTaskTimer(KitpvpPlugin.plugin, runnable, delay.toLong(), interval.toLong())
    }

    fun runLater(runnable: Runnable, delay: Int) {
        Bukkit.getScheduler().runTaskLater(KitpvpPlugin.plugin, runnable, delay.toLong())
    }

    fun runLaterAsync(runnable: Runnable, delay: Int) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(KitpvpPlugin.plugin, runnable, delay.toLong())
    }

    fun runLaterAsync(runnable: Runnable, delay: Int, executorService: ExecutorService) {
        executorService.execute {
            Bukkit.getScheduler().runTaskLater(
                KitpvpPlugin.plugin,
                runnable,
                delay.toLong()
            )
        }
    }
}