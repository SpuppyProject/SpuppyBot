package com.github.yeoj34760.spuppybot.db

import com.github.yeoj34760.spuppy.command.Commands
import com.github.yeoj34760.spuppybot.db.DB.Connection

object CommandDBController {
    fun fromCommands(): List<Commands> {
        var temp = mutableListOf<Commands>()
        val t = Connection().createStatement().executeQuery("select name, command from command")
        while (t.next()) {
            if (checkCommands(temp, t.getString(1)))
                fromCommands(temp, t.getString(1))!!.aliases!!.add(t.getString(2))
            else {
                val tempCommands = Commands(t.getString(1))
                tempCommands.aliases.add(t.getString(2))
                temp.add(tempCommands)
            }
        }

        return temp
    }

    private fun checkCommands(commandsList: List<Commands>, name: String): Boolean {
        for (commands in commandsList) {
            if (commands.name == name)
                return true
        }

        return false
    }

    private fun fromCommands(commandsList: List<Commands>, name: String): Commands? {
        for (commands in commandsList) {
            if (commands.name == name)
                return commands
        }

        return null
    }
}