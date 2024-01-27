package TelegramBot

import java.io.File
import kotlin.math.roundToInt

const val LIMIT = 3
fun main() {

    val text = File("words.txt")

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
        try {
            val userNumber = readln().toInt()
            when (userNumber) {
                1 -> {
                    val remainsWord = dictionary.filter { it.correctAnswersCount < LIMIT }
                    if (remainsWord.isEmpty())
                        println("Выучены все слова")
                    else {
                        do {
                            println("Учим дальше?  если хотите завершить программу нажмите- Й")
                            val user = readln()
                            if (user.equals("Й", ignoreCase = true))
                                return
                            val listOriginal = remainsWord.map { it.engWord }
                            println((listOriginal.random().uppercase()))
                            println("Выберите вариант ответа из списка: ")
                            remainsWord.forEachIndexed { ind, el -> println("${ind + 1}  ${el.rusWord} ") }
                        } while (user != "P")
                    }
                }

                2 -> {
                    val learnWord = dictionary.filter { it.correctAnswersCount >= LIMIT }.size
                    println("$learnWord из ${dictionary.size} | ${((learnWord.toFloat() / dictionary.size) * 100).roundToInt()}%")
                }

                0 -> break
                else -> println("Вы ввели некоректное число")
            }
        } catch (e: NumberFormatException) {
            println("Введен неправильый формат числа")
        }
    }
}

data class Word(val engWord: String, val rusWord: String?, val correctAnswersCount: Int = 0) {
    override fun toString(): String {
        return "\nWord(engWord='$engWord', rusWord=$rusWord, correctAnswersCount=$correctAnswersCount)"
    }
}