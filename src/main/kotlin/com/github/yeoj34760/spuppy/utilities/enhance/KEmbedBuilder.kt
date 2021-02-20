package com.github.yeoj34760.spuppy.utilities.enhance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

class KEmbedBuilder {
    data class Author(var name: String? = null, var url: String? = null, var iconUrl: String? = null)
    data class Footer(var text: String = "", var iconUrl: String? = null)
    data class Field(var name: String? = null, var value: String? = null, var inline: Boolean = false)

    private var author: Author? = null
    private var footer: Footer? = null
    var color: Int? = null
    var description: String? = null
    var image: String? = null
    var thumbnail: String? = null
    var fields: MutableList<MessageEmbed.Field> = mutableListOf()

    fun author(block: Author.() -> Unit) {
        author = (author ?: Author()).apply(block)
    }

    fun footer(block: Footer.() -> Unit) {
        footer = (footer ?: Footer()).apply(block)
    }

    fun addField(block: Field.() -> Unit) {
        val tempBlock = Field().apply(block)
        fields.add(MessageEmbed.Field(tempBlock.name, tempBlock.value, tempBlock.inline))
    }

    fun addBlankField(inline: Boolean) {
        fields.add(MessageEmbed.Field(null, null, inline))
    }

    fun build(): MessageEmbed {
        val embedBuilder = EmbedBuilder()
        if (author != null)
            embedBuilder.setAuthor(author!!.name, author!!.url, author!!.iconUrl)

        if (footer != null)
            embedBuilder.setFooter(footer!!.text, footer!!.iconUrl)

        if (color != null)
            embedBuilder.setColor(color!!)

        if (description != null)
            embedBuilder.setDescription(description)

        if (image != null)
            embedBuilder.setImage(image)

        if (thumbnail != null)
            embedBuilder.setThumbnail(thumbnail)

        if (fields.isNotEmpty())
            for (field in fields)
                embedBuilder.addField(field)

        return embedBuilder.build()
    }
}