package tech.riotcode.kitpvp.util.command.adapter.defaults

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import tech.riotcode.kitpvp.util.command.adapter.CommandAdapter

class IntAdapter : CommandAdapter<Int> {

    override fun transform(sender: CommandSender, value: String): Int {
        return value.toInt()
    }

    override fun onError(commandSender: CommandSender, argument: String, throwable: Throwable) {
        commandSender.sendMessage("${ChatColor.RED}'$argument' is not a valid number.")
    }

    override fun getTransformType(): Class<*> = Int::class.java
}