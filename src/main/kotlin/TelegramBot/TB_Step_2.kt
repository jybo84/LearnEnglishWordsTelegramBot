package TelegramBot

import java.io.File

fun main() {

    val book = mutableListOf<Word>()
    val newWord = File("words.txt")
    newWord.createNewFile()
    newWord.writeText("door| дверь |")
    newWord.appendText("\nwall| стена|")

    val listWord = newWord.readLines()
    for (el in listWord) {
        val element = el.split("|")
        val word = Word(element[0], element[1], 3)

        book.add(word)
    }
    println(book.joinToString("\n"))
}

data class Word(
    val englishWord: String,
    val russianWord: String,
    var correctAnswersCount: Int = 0
)