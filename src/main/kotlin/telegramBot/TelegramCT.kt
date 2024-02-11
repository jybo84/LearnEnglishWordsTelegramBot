package telegramBot

fun main(args: Array<String>) {

    val service = TelegramBotService()
    val botToken = args[0]
    var updateId = 0

    while (true) {

        Thread.sleep(2000)
        val updates: String = service.getUpdates(botToken, updateId)
        val startUpdateId = updates.lastIndexOf("update_id")
        val endUpdateId = updates.lastIndexOf(",\n\"message\"")
        if (startUpdateId == -1 || endUpdateId == -1) continue
        val updateIdString = updates.substring(startUpdateId + 11, endUpdateId)

        updateId = updateIdString.toInt() + 1

        val idRegex = Regex("\\d{10}")
        val matchResultId: MatchResult? = idRegex.find(updates)
        val groupsId = matchResultId?.groups
        val chatId = groupsId?.get(0)?.value
        if (chatId != null) {
            println("massageID: $chatId ${service.sendMessage(updates, chatId)}")
        }
    }
}


