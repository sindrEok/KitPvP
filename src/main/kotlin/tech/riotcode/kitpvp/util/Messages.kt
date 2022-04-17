package tech.riotcode.kitpvp.util

import tech.riotcode.kitpvp.KitpvpPlugin

enum class Messages(val path: String, var message: String) {
    Information("information", "use /n for new line nerds"),

    SoupMessage("default.soupMessage", "Default Soup Message"),

    HostMessage(
        "event.hostMessage",
        "&5%hoster% &7is hosting a &3%eventName% &7 event. Starting in &3%seconds%&7, type &3/%eventName%&7 join, to join or"
    ),
    HostEventStarted("event.hostStarted", "&7The &3%eventName% event has started!"),
    EventWinMessage("event.winMessage", "&3%player%&7 has won the &4%eventName% &7event!"),

    SumoStartMessage("sumo.startMessage", ""),
    SumoTimerMessage("sumo.timerMessage", "The sumo match is starting in %seconds% second(s)"),

    KillstreakReachedMessage("killstreak.reached", "%player% has reached a killstreak of %killstreak%"),
    KillstreakEndedMessage("killstreak.ended", "%player% died with a killstreak of %killstreak%"),

    CooldownExpiredMessage("cooldown.expired", "&cYour cooldown has expired."),
    CooldownAbilityMessage("cooldown.ability", "&cYou are still on Cooldown for %cooldown% second(s)");

    val formattedMessage
        get() = CC.of(message)

    companion object {
        fun writeDefaults() {
            val plugin = KitpvpPlugin.plugin

            values().forEach {
                if (plugin.messageConfig.get(it.path) == null) {
                    plugin.messageConfig.set(it.path, it.message)
                }
            }
            plugin.messageConfig.save()
        }

        fun loadMessages() {
            val plugin = KitpvpPlugin.plugin

            plugin.messageConfig.load(plugin.messageConfig.file)

            values().forEach {
                it.message = plugin.messageConfig.getString(it.path) ?: "Message failed to load"
            }
        }
    }
}


