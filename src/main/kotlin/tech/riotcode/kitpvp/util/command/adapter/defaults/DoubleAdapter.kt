package tech.riotcode.kitpvp.util.command.adapter.defaults

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import tech.riotcode.kitpvp.util.command.adapter.CommandAdapter

class DoubleAdapter : CommandAdapter<Double> {

    override fun transform(sender: CommandSender, value: String): Double = value.toDouble()

    override fun onError(commandSender: CommandSender, argument: String, throwable: Throwable) {
        commandSender.sendMessage("${ChatColor.RED}'$argument' is not a valid number.")
    }

    override fun getTransformType(): Class<*> = Double::class.java
}