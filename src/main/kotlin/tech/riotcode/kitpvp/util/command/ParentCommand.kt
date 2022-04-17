package tech.riotcode.kitpvp.util.command

import org.apache.commons.lang.Validate
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import tech.riotcode.kitpvp.util.command.adapter.Argument
import tech.riotcode.kitpvp.util.command.adapter.CommandAdapter
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Parameter
import java.util.*
import java.util.concurrent.Executors
import java.util.stream.Collectors

class ParentCommand(var commandWrapper: CommandWrapper) : Command(
    commandWrapper.label,
    commandWrapper.command.description,
    "/${commandWrapper.label}",
    Collections.emptyList()
) {

    companion object {
        val EXECUTOR = Executors.newSingleThreadExecutor()
    }

    private val commandManager = CommandManager

    private val subCommands: MutableList<SubCommand> = ArrayList()

    fun registerSubCommand(subCommand: SubCommand) {
        subCommands.add(subCommand)
    }

    override fun execute(sender: CommandSender, s: String, args: Array<String>): Boolean {
        if (args.isNotEmpty()) {
            val subLowered: String = args[0].lowercase(Locale.getDefault())
            val optionalCommandWrapper = subCommands.stream().filter { subCommand ->
                val wrapper = subCommand.commandWrapper
                val sub: String = wrapper.label.split(".")[1].lowercase(Locale.getDefault())
                sub == subLowered
            }.findAny()
            if (optionalCommandWrapper.isPresent) {
                val argWrapper = optionalCommandWrapper.get().commandWrapper
                val stringList: MutableList<String> = ArrayList(listOf(*args))
                stringList.removeFirst()
                val arguments = stringList.toTypedArray()
                if (argWrapper.command.async) {
                    EXECUTOR.execute(
                        executeRunnable(
                            sender, argWrapper.label
                                .replace(".", " "), arguments, argWrapper
                        )
                    )
                    return true
                }

                executeRunnable(
                    sender, argWrapper.label.replace(".", " "),
                    arguments, argWrapper
                ).run()
                return true
            }
        }

        if (commandWrapper.command.async) {
            EXECUTOR.execute(executeRunnable(sender, s, args, commandWrapper))
            return true
        }

        executeRunnable(sender, s, args, commandWrapper).run()
        return true
    }

    @Suppress("UNCHECKED_CAST")
    private fun executeRunnable(sender: CommandSender, label: String, args: Array<String>, wrapper: CommandWrapper):
            Runnable {
        return Runnable {
            if (wrapper.hasPermission() && !sender.hasPermission(wrapper.command.permission)) {
                sender.sendMessage(CommandManager.NO_PERMISSIONS)
                return@Runnable
            }

            if (wrapper.command.playersOnly && sender !is Player) {
                sender.sendMessage("&cThis command is for players only!")
                return@Runnable
            }

            if (wrapper.isHelp()) {
                val commandWrappers: MutableList<CommandWrapper> = subCommands.map { it.commandWrapper }.toMutableList()
                commandWrappers.add(commandWrapper)
                sender.sendMessage(buildUsage(commandWrappers.stream()
                    .filter { w: CommandWrapper -> !w.isHelp() }
                    .filter { w: CommandWrapper ->
                        (w.command.permission.isEmpty()
                                || sender.hasPermission(w.command.permission))
                    }
                    .collect(Collectors.toList<Any?>()) as List<CommandWrapper>))
                return@Runnable
            }

            val parameters: ArrayList<Any> = ArrayList()
            parameters.add(CommandData(sender, args, label))
            if (wrapper.hasParameters()) {
                var i = 0
                while (i < wrapper.parameters.size) {
                    val parameter: Parameter = wrapper.parameters[i]
                    val argument: Argument = parameter.getAnnotation(Argument::class.java)
                    var value: StringBuilder
                    if (args.size > i) {
                        value = StringBuilder(args[i].trim())
                    } else {
                        if (argument.def.isEmpty()) {
                            sender.sendMessage(
                                java.lang.String.format(
                                    CommandManager.USAGE,
                                    label + "${ChatColor.WHITE} ${wrapper.buildParameters()}"
                                )
                            )
                            return@Runnable
                        }

                        value = StringBuilder(argument.def)
                        if (value.toString().equals("self", ignoreCase = true)) {
                            value = StringBuilder(sender.name)
                        }
                    }
                    if (parameter.type == String::class.java && args.size > i && i == wrapper.parameters.size - 1) {
                        value = StringBuilder()
                        for (s in args.copyOfRange(i, args.size)) value.append(s).append(" ")
                    }
                    value = StringBuilder(value.toString().trim())
                    val commandAdapter: CommandAdapter<*> = commandManager.getAdapterByType(parameter.type)
                    try {
                        val cast: Any? = commandAdapter.transform(sender, value.toString())
                        parameters.add(cast!!)
                    } catch (e: Exception) {
                        commandAdapter.onError(sender, value.toString(), e)
                        return@Runnable
                    }
                    i++
                }
            }

            try {
                wrapper.method.invoke(wrapper.commandClass, *parameters.toArray())
            } catch (e: IllegalArgumentException) {
                sender.sendMessage("${ChatColor.RED}An error occurred while processing this command.")
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                sender.sendMessage("${ChatColor.RED}An error occurred while processing this command.")
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                sender.sendMessage("${ChatColor.RED}An error occurred while processing this command.")
                e.printStackTrace()
            }
        }
    }

    private fun buildUsage(commandWrappers: List<CommandWrapper>): String {
        return "${
            ChatColor.translateAlternateColorCodes(
                '&',
                "&7&m-----------------------------------------------"
            )
        }\n${ChatColor.RED}${ChatColor.BOLD}" +
                "${capitalize(commandWrapper.label)} Help\n" +
                commandWrappers.stream().map<String> { wrapper: CommandWrapper ->
                    buildHelp(
                        wrapper,
                        if (wrapper.label.contains(".")) wrapper.label.replace(".", " ") else wrapper.label
                    )
                }.collect(Collectors.joining("\n")) + "\n" + ChatColor.translateAlternateColorCodes(
            '&',
            "&7&m-----------------------------------------------"
        )
    }

    private fun capitalize(string: String): String? {
        return string.substring(0, 1).uppercase(Locale.getDefault()) + string.substring(1)
    }

    private fun buildHelp(wrapper: CommandWrapper, label: String): String? {
        return "${ChatColor.RED}/${label}${(if (wrapper.hasParameters()) " " else "")}${wrapper.buildParameters()}" +
                "${ChatColor.GRAY} - ${ChatColor.WHITE}${wrapper.command.purpose}"
    }

    @Throws(java.lang.IllegalArgumentException::class)
    override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): List<String> {
        if (args.isNotEmpty()) {
            val optionalCommandWrapper =
                subCommands.stream().map { it.commandWrapper }.filter { wrapper: CommandWrapper ->
                    val sub: String = wrapper.label.split(".")[1]
                    sub.equals(args[0], ignoreCase = true)
                }.findAny()
            if (optionalCommandWrapper.isPresent) {
                val argWrapper = optionalCommandWrapper.get()
                val stringList: MutableList<String> = ArrayList(listOf(*args))
                stringList.removeAt(0)
                val newArgs = stringList.toTypedArray()
                return handleTabComplete(sender, argWrapper.label.replace(".", " "), newArgs, argWrapper)
            }
        }
        return handleTabComplete(sender, commandWrapper.label, args, commandWrapper)
    }

    private fun handleTabComplete(
        sender: CommandSender,
        label: String,
        args: Array<String>,
        wrapper: CommandWrapper
    ): List<String> {
        if (args.isEmpty() || wrapper.parameters.isEmpty()) {
            if (wrapper == commandWrapper) {
                if (subCommands.isEmpty()) return ArrayList(defaultTabComplete(sender, label, args))
                if (args.isNotEmpty()) {
                    val start = args[0].lowercase(Locale.getDefault())
                    return subCommands.stream()
                        .map { it.commandWrapper }
                        .filter { w: CommandWrapper ->
                            (w.command.permission.isEmpty()
                                    || sender.hasPermission(w.command.permission))
                        }
                        .map(CommandWrapper::label)
                        .map { string: String -> string.split(".")[1] }
                        .filter { string: String -> string.lowercase(Locale.getDefault()).startsWith(start) }
                        .sorted(java.lang.String.CASE_INSENSITIVE_ORDER)
                        .collect(Collectors.toCollection { ArrayList() })
                }
                return subCommands.stream()
                    .map { it.commandWrapper }
                    .filter { w: CommandWrapper ->
                        (w.command.permission.isEmpty()
                                || sender.hasPermission(w.command.permission))
                    }
                    .map(CommandWrapper::label)
                    .map { string: String -> string.split(".")[1] }
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .collect(Collectors.toCollection { ArrayList() })
            }
            return ArrayList(defaultTabComplete(sender, label, args))
        }
        return try {
            val parameter: Parameter = wrapper.parameters[args.size - 1]
            val adapter: CommandAdapter<*> = commandManager.getAdapterByType(parameter.type)
            ArrayList<String>(adapter.tabComplete(sender, args[args.size - 1]).toMutableList())
        } catch (e: IndexOutOfBoundsException) {
            ArrayList(defaultTabComplete(sender, label, args))
        }
    }

    private fun defaultTabComplete(sender: CommandSender, label: String, args: Array<String>): ArrayList<String> {
        Validate.notNull(sender, "Sender cannot be null")
        Validate.notNull(args, "Arguments cannot be null")
        Validate.notNull(label, "Alias cannot be null")
        val senderPlayer = if (sender is Player) sender else null
        val matchedPlayers = ArrayList<String>()
        for (player in sender.server.onlinePlayers) {
            val name = player.name
            if (args.isNotEmpty()) {
                if ((senderPlayer == null || senderPlayer.canSee(player)) &&
                    StringUtil.startsWithIgnoreCase(name, args[args.size - 1])
                ) {
                    matchedPlayers.add(name)
                }
            } else {
                matchedPlayers.add(name)
            }
        }
        matchedPlayers.sortWith(java.lang.String.CASE_INSENSITIVE_ORDER)
        return matchedPlayers
    }
}