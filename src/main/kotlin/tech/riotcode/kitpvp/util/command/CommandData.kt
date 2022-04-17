package tech.riotcode.kitpvp.util.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class CommandData(val sender: CommandSender, val args: Array<String>, val label: String) {

    fun isPlayer() = sender is Player

    fun getPlayer() = sender as Player

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandData

        if (sender != other.sender) return false
        if (!args.contentEquals(other.args)) return false
        if (label != other.label) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sender.hashCode()
        result = 31 * result + args.contentHashCode()
        result = 31 * result + label.hashCode()
        return result
    }
}