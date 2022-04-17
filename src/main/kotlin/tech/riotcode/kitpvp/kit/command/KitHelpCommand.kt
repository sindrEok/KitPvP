package tech.riotcode.kitpvp.kit.command

import tech.riotcode.kitpvp.util.command.CommandClass
import tech.riotcode.kitpvp.util.command.CommandData
import tech.riotcode.kitpvp.util.command.annotate.Command

class KitHelpCommand : CommandClass() {

    @Command(["kit.help"], showHelp = true)
    fun help(commandData: CommandData) {
        commandData.sender.sendMessage(
            if (commandData.sender.hasPermission("kit.admin")) {
                arrayOf(
                    "---------------------",
                    "Kit Command:",
                    "/kit",
                    "/kit <KitName>",
                    "---------------------",
                    "ADMIN COMMANDS:",
                    "---------------------",
                )
            } else {
                arrayOf(
                    "---------------------",
                    "Kit Command:",
                    "/kit",
                    "/kit <KitName>",
                    "---------------------",
                )
            }
        )
    }

}