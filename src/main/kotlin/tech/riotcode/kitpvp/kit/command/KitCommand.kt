package tech.riotcode.kitpvp.kit.command

import tech.riotcode.kitpvp.extensions.profile
import tech.riotcode.kitpvp.kit.KitHandler
import tech.riotcode.kitpvp.kit.menu.SelectKitMenu
import tech.riotcode.kitpvp.util.command.CommandClass
import tech.riotcode.kitpvp.util.command.CommandData
import tech.riotcode.kitpvp.util.command.annotate.Command

class KitCommand : CommandClass() {

    @Command(["kit"], playersOnly = true)
    fun default(commandData: CommandData) {
        val profile = commandData.getPlayer().profile

        val args = commandData.args

        if (args.isNotEmpty() && args[0] != "") {
            val findKit = KitHandler.findKit(args[0])

            if (findKit != null) {
                findKit.apply(profile)
                commandData.sender.sendMessage("applied kit $findKit")
            } else {
                commandData.sender.sendMessage("Kit doesn't exist")
            }
            return
        }
        SelectKitMenu(commandData.getPlayer()).open()
    }

}