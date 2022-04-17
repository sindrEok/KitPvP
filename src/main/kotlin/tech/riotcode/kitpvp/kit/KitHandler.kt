package tech.riotcode.kitpvp.kit

import tech.riotcode.kitpvp.KitpvpPlugin.Companion.plugin
import tech.riotcode.kitpvp.database.MongoHandler
import tech.riotcode.kitpvp.kit.ability.activation.ActivationHandler
import tech.riotcode.kitpvp.kit.impl.AlphaKit
import tech.riotcode.kitpvp.kit.impl.DragonKit
import tech.riotcode.kitpvp.kit.impl.HedgehogKit
import tech.riotcode.kitpvp.kit.impl.MainKit
import tech.riotcode.kitpvp.profile.Profile
import tech.riotcode.kitpvp.util.CooldownUtil
import java.util.*

object KitHandler {

    val kitMap = HashMap<String, Kit>().apply {
        this["Main"] = MainKit()
        this["Hedgehog"] = HedgehogKit()
        this["Dragon"] = DragonKit()
        this["Alpha"] = AlphaKit()
    }

    val kitCooldown = CooldownUtil()

    val activationHandler = ActivationHandler()

    init {
        loadKits()
    }

    fun loadKits() {
        kitMap.values.forEach {
            loadKit(it, false)
        }
    }

    fun saveKits() {
        kitMap.values.forEach {
            saveKit(it, false)
        }
    }

    private fun saveKit(kit: Kit, async: Boolean) {
        MongoHandler.documentSave(
            "kits",
            plugin.jsonHelper.gson.toJson(kit),
            "name", kit.toString(),
            async
        )
    }

    private fun loadKit(kit: Kit, async: Boolean) {
        MongoHandler.documentLoad(
            {
                it?.let {
                    kitMap[kit.toString()] = plugin.jsonHelper.gson.fromJson(it.toJson(), kit.javaClass)
                }
            }, "kits", "name", kit.toString(), async
        )
    }

    fun findKit(string: String): Kit? = kitMap[string.lowercase(Locale.getDefault())]

    fun applyKit(kit: Kit, profile: Profile) {
        profile.currentKit = kit
        val get = profile.player.get() ?: throw Exception("What the fuck?")

        kit.inventory.forEachIndexed { index, itemStack ->
            get.inventory.setItem(index, itemStack)
        }
        get.inventory.armorContents = kit.armor
    }

    fun purgePlayer(profile: Profile) {
        profile.currentKit = null
        kitCooldown.removeCooldown(profile.uuid)
        activationHandler.removePlayer(profile.uuid)
    }
}