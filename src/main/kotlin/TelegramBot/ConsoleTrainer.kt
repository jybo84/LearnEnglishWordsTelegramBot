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
            var userNumber = readln().toInt()
            when (userNumber) {
                1 -> {
                    val remainsWord = dictionary.filter { it.correctAnswersCount < LIMIT }
                    if (remainsWord.isEmpty())
                        println("Выучены все слова")
                    else {
                        do {
                            val newListForUser = remainsWord.shuffled().take(4)

                            val wordForUser = newListForUser.map { it }.random()
                            println()
                            println(wordForUser.engWord.uppercase())
                            println("Выберите вариант ответа из списка: ")
                            newListForUser.forEachIndexed { index, el -> println("${index + 1} - ${el.rusWord} ") }
                            println()
                            println("0 - выйти в меню")
                            val userChoice = readln().toInt()

                            fun checkUserAnswer(number: Int) {
                                if (wordForUser == newListForUser[userNumber])
                                    println("ПРАВИЛЬНО")
                                else println(
                                    "НЕ ВЕРНО.  " +
                                            "Вы выбрали ${(newListForUser[userNumber].rusWord)?.uppercase()} " +
                                            "Правильный ответ ${(wordForUser.rusWord)?.uppercase()}"
                                )
                            }

                            when (userChoice) {
                                1 -> checkUserAnswer(userNumber)
                                2 -> checkUserAnswer(userNumber)
                                3 -> checkUserAnswer(userNumber)
                                4 -> checkUserAnswer(userNumber)
                                0 -> break
                                else -> println("неправильно ввели число")
                            }
                        } while (userNumber != 0)
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


data class Word(val engWord: String, val rusWord: String?, val correctAnswersCount: Int = 0)