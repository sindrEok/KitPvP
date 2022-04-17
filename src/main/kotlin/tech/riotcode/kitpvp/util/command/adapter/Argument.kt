package tech.riotcode.kitpvp.util.command.adapter

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class Argument(val name: String = "", val def: String = "")
