package tech.riotcode.kitpvp.util.message.clickable

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent

class ClickableBuilder(baseMessage: String) {

    private var textComponent: TextComponent = TextComponent(baseMessage)

    fun setCommand(command: String): ClickableBuilder {
        textComponent.clickEvent =
            ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
        return this
    }

    fun setHover(vararg hoverMessage: String): ClickableBuilder {
        textComponent.hoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            hoverMessage.map { TextComponent("$it\n") }.toTypedArray()
        )
        return this
    }

    fun create(): TextComponent {
        return textComponent
    }
}