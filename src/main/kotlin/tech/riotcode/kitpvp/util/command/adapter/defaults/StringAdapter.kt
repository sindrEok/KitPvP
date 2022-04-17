package tech.riotcode.kitpvp.util.command.adapter.defaults

import org.bukkit.command.CommandSender
import tech.riotcode.kitpvp.util.command.adapter.CommandAdapter

class StringAdapter : CommandAdapter<String> {

    override fun transform(sender: CommandSender, value: String): String {
        return value
    }

    override fun onError(commandSender: CommandSender, argument: String, throwable: Throwable) {
        // Won't happen
    }

    override fun getTransformType(): Class<*> = String::class.java
}