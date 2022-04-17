package tech.riotcode.kitpvp.util

import org.bukkit.ChatColor

object CC {
    fun of(string: String): String = ChatColor.translateAlternateColorCodes('&', string)
}