package tech.riotcode.kitpvp.util.command.annotate

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Command(
    val label: Array<String>,
    val description: String = "A Factions Command!",
    val purpose: String = "Default Factions Command",
    val permission: String = "",
    val showHelp: Boolean = false,
    val playersOnly: Boolean = false,
    val async: Boolean = false
)