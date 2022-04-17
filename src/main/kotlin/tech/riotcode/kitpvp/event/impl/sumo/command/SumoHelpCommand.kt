package tech.riotcode.kitpvp.event.impl.sumo.command

import tech.riotcode.kitpvp.util.command.CommandClass
import tech.riotcode.kitpvp.util.command.CommandData
import tech.riotcode.kitpvp.util.command.annotate.Command

class SumoHelpCommand : CommandClass() {

    @Command(["sumo"], showHelp = true)
    fun help(commandData: CommandData) {
        if (commandData.sender.hasPermission("sumo.adminhelp")) {
            commandData.sender.sendMessage(
                arrayOf(
                    "---------------------",
                    "SUMO COMMANDS:",
                    "/sumo join",
                    "/sumo host",
                    "---------------------",
                    "ADMIN COMMANDS:",
                    "/sumo setspawnlocation <arena> <1/2>",
                    "/sumo setlobbylocation",
                    "/sumo createarena <name>",
                    "/sumo seticon <name>",
                    "/sumo delete <name>",
                    "---------------------",
                )
            )
        } else {
            commandData.sender.sendMessage(
                arrayOf(
                    "---------------------",
                    "SUMO COMMANDS:",
                    "/sumo join",
                    "/sumo host",
                    "---------------------",
                )
            )
        }
    }

}