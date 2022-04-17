package tech.riotcode.kitpvp.util.command.adapter.defaults

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.riotcode.kitpvp.util.command.adapter.CommandAdapter

class PlayerAdapter : CommandAdapter<Player> {

    override fun transform(sender: CommandSender, value: String): Player = Bukkit.getPlayer(value)

    override fun onError(commandSender: CommandSender, argument: String, throwable: Throwable) {
        commandSender.sendMessage("${ChatColor.RED}A player with the name '$argument' could not be found.")
    }

    override fun getTransformType(): Class<*> = Player::class.java
}