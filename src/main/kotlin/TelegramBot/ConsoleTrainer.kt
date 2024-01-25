package TelegramBot

import java.io.File

fun main() {

    val text = File("text.txt")
    text.createNewFile()
    text.writeText("hello |привет |2")
    text.appendText("\ndog |собака |2")
    text.appendText("\ncat |кошка |1")

    val dictionary = mutableListOf<Word>()

    val lines = text.readLines()
    for (el in lines) {
        val splitString = el.split(" |")
        val word = Word(splitString[0].trim(), splitString[1].trim(), splitString[2].toInt())
        dictionary.add(word)
    }
    dictionary.forEach { println(it) }
}

data class Word(val engWord: String, val rusWord: String?, val correctAnswersCount: Int = 0)

