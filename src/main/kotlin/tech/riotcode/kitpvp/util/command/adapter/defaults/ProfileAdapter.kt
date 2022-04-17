package tech.riotcode.kitpvp.util.command.adapter.defaults

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import tech.riotcode.kitpvp.extensions.profile
import tech.riotcode.kitpvp.profile.Profile
import tech.riotcode.kitpvp.util.command.adapter.CommandAdapter

class ProfileAdapter : CommandAdapter<Profile> {
    override fun transform(sender: CommandSender, value: String): Profile = Bukkit.getPlayer(value)!!.profile

    override fun onError(commandSender: CommandSender, argument: String, throwable: Throwable) {
        commandSender.sendMessage("${ChatColor.RED}Profile of player with name '$argument' could not be found!")
    }

    override fun getTransformType(): Class<*> = Profile::class.java
}