package tech.riotcode.kitpvp.util.command.adapter

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

interface CommandAdapter<T> {

    fun transform(sender: CommandSender, value: String): T

    fun onError(commandSender: CommandSender, argument: String, throwable: Throwable)

    fun tabComplete(sender: CommandSender, argument: String): Collection<String> {
        val senderPlayer = if (sender is Player) sender else null
        val matchedPlayers = ArrayList<String>()
        for (player in sender.server.onlinePlayers) {
            val name = player.name
            if ((senderPlayer == null || senderPlayer.canSee(player)) &&
                StringUtil.startsWithIgnoreCase(name, argument)
            ) {
                matchedPlayers.add(name)
            }
        }
        matchedPlayers.sortWith(String.CASE_INSENSITIVE_ORDER)
        return matchedPlayers
    }

    fun getTransformType(): Class<*>
}