package tech.riotcode.kitpvp.util.command.adapter.defaults

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import tech.riotcode.kitpvp.util.command.adapter.CommandAdapter

class LongAdapter : CommandAdapter<Long> {

    override fun transform(sender: CommandSender, value: String): Long {
        return value.toLong()
    }

    override fun onError(commandSender: CommandSender, argument: String, throwable: Throwable) {
        commandSender.sendMessage("${ChatColor.RED}'$argument' is not a valid number.")
    }

    override fun getTransformType(): Class<*> = Long::class.java
}