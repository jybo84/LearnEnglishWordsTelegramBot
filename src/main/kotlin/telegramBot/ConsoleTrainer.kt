package telegramBot

import java.io.File
import kotlin.math.roundToInt

const val LIMIT_OF_LEARNED_WORD = 1
const val MAX_LIST_WORD_FOR_USER = 4

fun main() {

    val text = File("words.txt")
    val dictionary = mutableListOf<Word>()
    val lines = text.readLines()
    for (line in lines) {
        val splitString = line.split("|")
        val word = Word(
            engWord = splitString[0].trim(),
            rusWord = splitString[1].trim(),
            splitString[2].toIntOrNull() ?: 0
        )
        dictionary.add(word)
    }

    while (true) {
        println(
            """
                
            МЕНЮ:
        1 - Учить слова
        2 - Статистика
        0 - Выход
    """.trimIndent()
        )
        println("Выберите пункт меню \n")
        when (readln().toIntOrNull()) {
            1 -> {
                while (true) {
                    val remainsWord = dictionary.filter { it.correctAnswersCount < LIMIT_OF_LEARNED_WORD }
                    if (remainsWord.isEmpty()) {
                        println("ВЫ ВЫУЧИЛИ ВСЕ  \n")
                        break
                    }
                    val newListForUser = remainsWord.shuffled().take(MAX_LIST_WORD_FOR_USER)
                    val wordForUser = newListForUser.random()

                    println(wordForUser.engWord.uppercase())
                    println("Выберите вариант ответа из списка: \n")
                    newListForUser.forEachIndexed { index, el -> println("${index + 1} - ${el.rusWord} ") }

                    println("\n0 - выйти в меню")

                    when (val userChoice = readln().toIntOrNull()) {
                        in 1..MAX_LIST_WORD_FOR_USER -> {
                            val correctAnswerIndex = newListForUser.indexOf(wordForUser)
                            if (correctAnswerIndex + 1 == userChoice) {
                                println("\u001B[32mПРАВИЛЬНО\u001B[37m")
                                wordForUser.correctAnswersCount++
                                saveDictionary(dictionary)


                            } else {
                                println(
                                    "\u001B[31mНЕ ВЕРНО.\u001B[37m  " +
                                            "Вы выбрали ${
                                                (userChoice?.minus(1)
                                                    ?.let { newListForUser[it] }?.rusWord)?.uppercase() ?: ""
                                            } " +
                                            "Правильный ответ ${(wordForUser.rusWord).uppercase()}"
                                )
                            }
                        }

                        0 -> break
                        else -> println("неправильно ввели число")
                    }
                }

            }

            2 -> {
                val learnWord = dictionary.filter { it.correctAnswersCount >= LIMIT_OF_LEARNED_WORD }.size
                println("$learnWord из ${dictionary.size} | ${((learnWord.toFloat() / dictionary.size) * 100).roundToInt()}%")
            }

            0 -> break
            else -> println("Вы ввели некорректное значение")
        }
    }
}

fun saveDictionary(dictionary: List<Word>) {
    val dictionaryFile = File("words.txt")
    dictionaryFile.writeText("")
    dictionary.forEach { dictionaryFile.appendText("${it.engWord}|${it.rusWord}|${it.correctAnswersCount}\n") }
}

data class Word(
    val engWord: String,
    val rusWord: String,
    var correctAnswersCount: Int = 0,
)