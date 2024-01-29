package TelegramBot

import java.io.File
import kotlin.math.roundToInt


const val LIMIT = 3
fun main() {

    val text = File("words.txt")
    text.writeText("hello |привет |5")
    text.appendText("\ndog |собака |5")
    text.appendText("\ncat |кошка |2")

    val dictionary = mutableListOf<Word>()

    val lines = text.readLines()
    for (el in lines) {
        val splitString = el.split(" |")
        val word = Word(splitString[0].trim(), splitString[1].trim(), splitString[2].toInt())
        dictionary.add(word)
    }

    println(
        """
            МЕНЮ:
        1 - Учить слова
        2 - Статистика
        0 - Выход 
    """.trimIndent()
    )

    while (true) {
        println("Выберите пункт меню")
        val userNumber = readln().toInt()

        when (userNumber) {
            1 -> TODO()
            2 -> {
                val learnWord = dictionary.filter { it.correctAnswersCount >= LIMIT }.size
                println("$learnWord из ${dictionary.size} | ${((learnWord.toFloat() / dictionary.size) * 100).roundToInt()}%")
            }
            0 -> break
            else -> println("Вы ввели некорекное число")
        }
    }
}

data class Word(val engWord: String, val rusWord: String?, val correctAnswersCount: Int = 0)
