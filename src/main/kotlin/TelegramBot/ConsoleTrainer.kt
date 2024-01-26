package TelegramBot

import java.io.File
import kotlin.math.roundToInt


const val LIMIT = 3
fun main() {

    val text = File("words.txt")
    text.writeText("hello |привет |5")
    text.appendText("\ndog |собака |1")
    text.appendText("\ncat |кошка |1")
    text.appendText("\nred |красный |1")
    text.appendText("\npen |ручка |1")

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
            1 -> {
                val remainsWord = dictionary.filter { it.correctAnswersCount < LIMIT }
                if (remainsWord.isEmpty())
                    println("Выучены все слова")
                else {
                    val listOriginal = remainsWord.map { it.engWord }
                    val listTranslate = remainsWord.map { it.rusWord }
                    Thread.sleep(500)
                    println((listOriginal.random().uppercase()))
                    Thread.sleep(500)
                    println("Выберите вариант ответа из списка: ")
                    Thread.sleep(500)

                    val listTotalWord = listTranslate.shuffled().take(4)
                    listTotalWord.forEachIndexed { ind, el -> println("${ind + 1} $el") }
                }
            }

            2 -> {
                val learnWord = dictionary.filter { it.correctAnswersCount >= LIMIT }.size
                println("$learnWord из ${dictionary.size} | ${((learnWord.toFloat() / dictionary.size) * 100).roundToInt()}%")
            }

            0 -> break

            else -> println("Вы ввели некоректное число")

        }
    }
}

data class Word(val engWord: String, val rusWord: String?, val correctAnswersCount: Int = 0) {
    override fun toString(): String {
        return "\nWord(engWord='$engWord', rusWord=$rusWord, correctAnswersCount=$correctAnswersCount)"
    }
}

