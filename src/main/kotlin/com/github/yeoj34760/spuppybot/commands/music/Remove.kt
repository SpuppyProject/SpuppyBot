package com.github.yeoj34760.spuppybot.commands.music

object Remove : Command() {
    init {
        name = "remove"
        aliases = arrayOf("remove", "삭제", "re", "ㄱㄷ", "그", "ㄱ드ㅐㅍㄷ")
    }

    override fun execute(event: CommandEvent) {
        val playerControl = GuildManager.playerControls[event.guild.idLong]
        if (playerControl == null || playerControl.trackQueue.isEmpty()) {
            event.reply("umm.. 리스트에 음악이 없네요")
            return
        }
        if (event.args.isEmpty() || event.args.toIntOrNull() == null) {
            event.reply("숫자를 올바르게 써주세요.")
            return
        }
        val number = event.args.toInt()
        playerControl
        when {
            number > playerControl.trackQueue.size -> event.reply("해당 넘버에서 음악이 없네요")
            number < 1 -> event.reply("1 미만을 입력하실 수 없습니다.")
            else -> {
                event.reply("`${playerControl.trackQueue.toTypedArray()[number - 1].info.title}`을(를) 삭제됨")
                playerControl.trackQueue.remove(playerControl.trackQueue.toTypedArray()[number - 1])
            }
        }
    }
}