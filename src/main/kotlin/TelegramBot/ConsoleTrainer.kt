package TelegramBot


import java.io.File
import kotlin.math.roundToInt

const val LIMIT = 3
const val MAX_LIST_WORD_FOR_USER = 4
fun main() {

    val text = File("words.txt")
    text.createNewFile()

    val dictionary = mutableListOf<Word>()
    val lines = text.readLines()
    for (el in lines) {
        val splitString = el.split(" |")
        val word = Word(splitString[0].trim(), splitString[1].trim(), splitString[2].toInt())
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
        println("Выберите пункт меню")
        try {
            val userNumber = readln().toInt()
            when (userNumber) {
                1 -> {
                    val remainsWord = dictionary.filter { it.correctAnswersCount < LIMIT }

                    if (remainsWord.isEmpty())
                        println("ВЫ ВЫУЧИЛИ ВСЕ СЛОВА")
                    else {
                        do {
                            val newListForUser = remainsWord.shuffled().take(MAX_LIST_WORD_FOR_USER)

                            val wordForUser = newListForUser.map { it }.random()
                            println()
                            println(wordForUser.engWord.uppercase())
                            println("Выберите вариант ответа из списка: ")
                            newListForUser.forEachIndexed { index, el -> println("${index + 1} - ${el.rusWord} ") }
                            println()
                            println("0 - выйти в меню")
                            val userChoice = readln().toInt()


                            fun checkUserAnswer(number: Int): List<Word> {
                                if (wordForUser.rusWord == newListForUser[userChoice - 1].rusWord) {
                                    println("ПРАВИЛЬНО")
                                    wordForUser.correctAnswersCount++

                                    //text.writeText(remainsWord.joinToString("\n"))
                                    text.writeText(dictionary.joinToString("\n"))

                                } else println(
                                    "НЕ ВЕРНО.  " +
                                            "Вы выбрали ${(newListForUser[userChoice].rusWord)?.uppercase()} " +
                                            "Правильный ответ ${(wordForUser.rusWord)?.uppercase()}"
                                )
                                return dictionary
                            }

                            when (userChoice) {
                                1 -> checkUserAnswer(userChoice)
                                2 -> checkUserAnswer(userChoice)
                                3 -> checkUserAnswer(userChoice)
                                4 -> checkUserAnswer(userChoice)
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

data class Word(val engWord: String, val rusWord: String?, var correctAnswersCount: Int = 0) {
    override fun toString(): String {
        return "$engWord |$rusWord |$correctAnswersCount"
    }
}