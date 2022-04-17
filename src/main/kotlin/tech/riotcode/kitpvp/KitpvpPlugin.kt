package tech.riotcode.kitpvp

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import tech.riotcode.kitpvp.event.EventHandler
import tech.riotcode.kitpvp.kit.KitHandler
import tech.riotcode.kitpvp.lobby.LobbyHandler
import tech.riotcode.kitpvp.profile.ProfileHandler
import tech.riotcode.kitpvp.util.ClassUtil
import tech.riotcode.kitpvp.util.Messages
import tech.riotcode.kitpvp.util.TaskScheduler
import tech.riotcode.kitpvp.util.YamlConfig
import tech.riotcode.kitpvp.util.command.CommandManager
import tech.riotcode.kitpvp.util.json.JsonHelper
import tech.riotcode.kitpvp.util.json.adapter.ArenaTypeAdapter
import tech.riotcode.kitpvp.util.json.adapter.ItemStackTypeAdapter
import tech.riotcode.kitpvp.util.json.adapter.UUIDTypeAdapter

class KitpvpPlugin : JavaPlugin() {

    companion object {
        val plugin: KitpvpPlugin by lazy {
            getPlugin(KitpvpPlugin::class.java)
        }
    }

    val jsonHelper: JsonHelper = JsonHelper(
        ItemStackTypeAdapter(),
        ArenaTypeAdapter(),
        UUIDTypeAdapter()
    )

    val messageConfig: YamlConfig by lazy { YamlConfig("messages") }
    val lobbyConfig: YamlConfig by lazy { YamlConfig("lobby") }

    override fun onEnable() {
        Messages.writeDefaults()
        Messages.loadMessages()

        this.saveResource("lobby.yml", false)

        setupListeners()

        CommandManager.start()

        saveTimers()

        Bukkit.getWorlds()[0].entities.forEach { it.remove() }
    }

    override fun onDisable() {
        messageConfig.save()
        lobbyConfig.save()

        EventHandler.close()
        ProfileHandler.close()
        KitHandler.saveKits()
        LobbyHandler.close()
    }

    private fun saveTimers() {
        TaskScheduler.runRepeating({
            ProfileHandler.saveProfiles(true)
        }, 5 * 60, 5 * 60)
        TaskScheduler.runRepeating({
            EventHandler.saveArenas(true)
        }, 60 * 60, 60 * 60)
    }

    private fun setupListeners() {
        ClassUtil.loadClassesFromPackage("tech.riotcode")
            .filter { !it.contains("$") }
            .filter { it.lowercase().contains("listener") }
            .forEach {
                val clazz = javaClass.classLoader.loadClass(it).kotlin

                if (ClassUtil.isSubclass(clazz, Listener::class)) {
                    server.pluginManager.registerEvents(clazz.objectInstance as Listener, this)
                }
            }
    }

}