package tech.riotcode.kitpvp.event.impl.sumo.task

import org.bukkit.Instrument
import org.bukkit.Note
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tech.riotcode.kitpvp.util.Messages

class StartTimerTask : BukkitRunnable() {

    var time: Int = 3

    private val players: MutableList<Player> = emptyList<Player>().toMutableList()

    fun addPlayer(player: Player) = players.add(player)

    override fun run() {
        if (time == 0) {
            this.cancel()
            return
        }
        players.forEach {
            it.sendMessage(
                Messages.SumoTimerMessage.formattedMessage
                    .replace("%seconds%", time.toString())
                    .replace("(s)", if ((time) == 1) "" else "s")
            )
            it.playNote(it.eyeLocation, Instrument.PIANO, Note.flat(1, Note.Tone.A))
        }
        time--
    }
}