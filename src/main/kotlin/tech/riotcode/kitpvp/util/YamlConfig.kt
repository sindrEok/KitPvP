package tech.riotcode.kitpvp.util

import org.bukkit.configuration.file.YamlConfiguration
import tech.riotcode.kitpvp.KitpvpPlugin.Companion.plugin
import java.io.File
import java.io.IOException

/*
 * Author: Bridge
 */

class YamlConfig(private val fileName: String) : YamlConfiguration() {

    val file: File = File(plugin.dataFolder, fileName)

    init {
        createFile()
    }

    private fun createFile() {
        try {
            if (!file.exists()) {
                if (plugin.getResource(fileName) != null)
                    plugin.saveResource(fileName, false)
                else save(file)

                load(file)
                return
            }

            load(file)
            save(file)
        } catch (e: IOException) {
            e.printStackTrace()
            throw IllegalArgumentException(
                "An IOException occurred while trying to create config file with " +
                        "name '$fileName'"
            )
        }
    }

    fun save() {
        save(file)
    }
}