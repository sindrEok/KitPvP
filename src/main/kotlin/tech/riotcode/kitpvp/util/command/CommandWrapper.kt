package tech.riotcode.kitpvp.util.command

import tech.riotcode.kitpvp.util.command.adapter.Argument
import tech.riotcode.kitpvp.util.command.annotate.Command
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.util.stream.Collectors

data class CommandWrapper(
    val label: String,
    val command: Command,
    val method: Method,
    val commandClass: CommandClass,
    val parameters: List<Parameter>
) {

    fun isHelp(): Boolean = command.showHelp

    fun hasPermission(): Boolean = command.permission.isNotEmpty()

    fun hasParameters(): Boolean = parameters.isNotEmpty()

    fun buildParameters(): String = parameters.stream().map { parameter: Parameter ->
        if (parameter.isAnnotationPresent(Argument::class.java))
            if (parameter.getDeclaredAnnotation(Argument::class.java).def.isNotEmpty())
                "[" + parameter.getDeclaredAnnotation(Argument::class.java).name + "]"
            else "<" + parameter.getDeclaredAnnotation(Argument::class.java).name + ">"
        else "<" + parameter.type.simpleName.lowercase() + ">"
    }.collect(Collectors.joining(" "))
}