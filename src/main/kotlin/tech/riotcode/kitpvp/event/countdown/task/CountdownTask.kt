package tech.riotcode.kitpvp.event.countdown.task

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tech.riotcode.kitpvp.event.EventEnum
import tech.riotcode.kitpvp.event.EventHandler
import tech.riotcode.kitpvp.util.CC
import tech.riotcode.kitpvp.util.Messages
import tech.riotcode.kitpvp.util.message.MessageBuilder
import tech.riotcode.kitpvp.util.message.clickable.ClickableBuilder
import java.util.*
import kotlin.properties.Delegates

class CountdownTask : BukkitRunnable() {

    var seconds by Delegates.notNull<Int>()
    lateinit var interval: Array<Int>
    lateinit var eventEnum: EventEnum
    lateinit var player: Player

    var active: Boolean = false

    private val formattedName = eventEnum.name.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    private val textComponent: TextComponent
        get() =
            MessageBuilder(
                Messages.HostMessage.formattedMessage
                    .replace("%player", player.name)
                    .replace("%seconds%", "$seconds second${if (seconds == 1) "" else "s"}")
                    .replace("%eventName%", formattedName)
            ).setColor(ChatColor.GRAY).addClickable(
                ClickableBuilder(CC.of(" &a[CLICK HERE]"))
                    .setCommand("/$formattedName join")
                    .setHover(CC.of("&aClick here to join"))
                    .create()
            ).create()


    override fun run() {
        if (seconds == 0) {
            Bukkit.broadcastMessage(Messages.HostEventStarted.formattedMessage)
            EventHandler.findEvent(eventEnum).start()
            return
        }
        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            if (interval.contains(seconds)) {
                onlinePlayer.spigot().sendMessage(textComponent)
            }
        }
        seconds--
    }
}
