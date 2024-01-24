package TelegramBot

import java.io.File

fun main() {

    val text = File("text.txt")
    text.createNewFile()
    text.writeText("hello |привет |0")
    text.appendText("\ndog |собака |0")
    text.appendText("\ncat |кошка |0")

    val dictionary = mutableListOf<Word>()

    val lines = text.readLines()
    for (el in lines) {
        val splitString = el.split(" |")
        val word = Word(splitString[0].trim(), splitString[1].trim(), correctAnswersCount = 2)
        dictionary.add(word)
    }
    dictionary.forEach { println(it) }
}

data class Word(val engWord: String, val rusWord: String?, val correctAnswersCount: Int = 0)

