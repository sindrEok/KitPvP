package tech.riotcode.kitpvp.util.command

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandMap
import org.bukkit.plugin.SimplePluginManager
import tech.riotcode.kitpvp.KitpvpPlugin.Companion.plugin
import tech.riotcode.kitpvp.util.ClassUtil
import tech.riotcode.kitpvp.util.MinecraftReflection
import tech.riotcode.kitpvp.util.command.adapter.CommandAdapter
import tech.riotcode.kitpvp.util.command.annotate.Command
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.full.primaryConstructor

object CommandManager {

    var NO_PERMISSIONS = "${ChatColor.RED}You do not have permission to perform this command."
    var PLAYERS_ONLY = "${ChatColor.RED}Players can only execute this command."
    var USAGE = "${ChatColor.GRAY}Usage: ${ChatColor.RED}/%s"


    private val commandMap: CommandMap? =
        if (Bukkit.getPluginManager() is SimplePluginManager) {
            MinecraftReflection.invokeField(
                MinecraftReflection.fetchField(SimplePluginManager::class.java, "commandMap")!!,
                Bukkit.getPluginManager()
            ) as CommandMap
        } else null

    private val parentCommands: MutableList<ParentCommand> = ArrayList()
    private val commandAdapters = ConcurrentHashMap<Class<*>, CommandAdapter<*>>()

    fun start() {
        registerAdapters("tech.riotcode")
        registerCommands("tech.riotcode")
    }

    private fun registerCommands(packageName: String) {
        val classLoader = plugin.javaClass.classLoader

        ClassUtil.loadClassesFromPackage(packageName)
            .filter { !it.lowercase().contains("extensions") }
            .filter { !it.contains("$") }
            .forEach {
                val clazz = classLoader.loadClass(it)
                if (ClassUtil.isSubclass(clazz.kotlin, CommandClass::class)) {
                    register(clazz.kotlin.primaryConstructor?.call() as CommandClass)
                }
            }
    }

    // Possible third party plugins can access this method too
    private fun registerAdapters(packageName: String) {
        val classLoader = plugin.javaClass.classLoader

        ClassUtil.loadClassesFromPackage(packageName)
            .filter { !it.lowercase().contains("extensions") }
            .filter { !it.contains("$") }
            .forEach {
                val clazz = classLoader.loadClass(it)

                if (ClassUtil.isSubclass(clazz.kotlin, CommandAdapter::class)) {
                    val commandAdapter = clazz.kotlin.primaryConstructor?.call() as CommandAdapter<*>
                    commandAdapters[commandAdapter.getTransformType()] = commandAdapter
                }
            }
    }

    fun register(commandClass: CommandClass) {
        commandClass::class.java.declaredMethods.forEach { method ->
            if (!method!!.isAnnotationPresent(Command::class.java))
                return@forEach

            val command: Command = method.getDeclaredAnnotation(Command::class.java)!!
            val parameters: MutableList<Parameter> = method.parameters.toMutableList()

            parameters.stream().filter { CommandData::class.java == it.type }.findAny().ifPresent(parameters::remove)

            command.label.forEach labelLoop@{
                val label = it.lowercase()

                if (isSubCommand(label)) {
                    if (parentExists(label)) {
                        val commandWrapper = CommandWrapper(label, command, method, commandClass, parameters)
                        getParent(label)?.registerSubCommand(SubCommand(commandWrapper))
                    } else {
                        val actualLabel = label.split(".")[0]
                        createParent(commandClass, method, command, parameters, actualLabel)
                        val wrapper = CommandWrapper(label, command, method, commandClass, parameters)
                        getParent(label)?.registerSubCommand(SubCommand(wrapper))
                    }
                    return@labelLoop
                }

                if (parentCommands.stream().anyMatch { c ->
                        c.label == label
                    }) {
                    parentCommands.stream()
                        .filter { cmd ->
                            cmd.label == label
                        }
                        .forEach { cmd ->
                            val wrapper = CommandWrapper(label, command, method, commandClass, parameters)

                            cmd.commandWrapper = wrapper
                        }
                } else {
                    createParent(commandClass, method, command, parameters, label)
                }
            }
        }
    }

    fun getAdapterByType(type: Class<*>): CommandAdapter<*> = commandAdapters[type]!!

    private fun isSubCommand(label: String) = label.contains('.')

    private fun createParent(
        commandClass: CommandClass,
        method: Method,
        command: Command,
        parameters: List<Parameter>,
        label: String
    ) {
        val commandWrapper = CommandWrapper(label, command, method, commandClass, parameters)
        val parent = ParentCommand(commandWrapper)
        commandMap!!.register("kitpvp", parent)
        parentCommands.add(parent)
    }

    private fun getParent(label: String): ParentCommand? {
        val actualLabel = label.split(".")[0]
        return parentCommands
            .firstOrNull { command: ParentCommand -> command.label.equals(actualLabel, true) }
    }

    private fun parentExists(label: String): Boolean {
        val actualLabel = label.split(".")[0]
        return parentCommands
            .any { it.label.equals(actualLabel, true) }
    }

}