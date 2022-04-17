package tech.riotcode.kitpvp.util.message

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent


class MessageBuilder(
    private val baseMessage: String = "",
    private val textComponent: TextComponent = TextComponent(baseMessage)
) {

    fun append(string: String): MessageBuilder {
        textComponent.addExtra(string)
        return this
    }

    fun newLine(): MessageBuilder {
        textComponent.addExtra(TextComponent("\n"))
        return this
    }

    fun addClickable(component: TextComponent): MessageBuilder {
        textComponent.addExtra(component)
        return this
    }

    fun setColor(chatColor: ChatColor): MessageBuilder {
        textComponent.color = chatColor
        return this
    }

    fun create(): TextComponent {
        return textComponent
    }

}