package telegramBot

import java.io.File
import kotlin.math.roundToInt

const val LIMIT_OF_LEARNED_WORD = 1
const val MAX_LIST_WORD_FOR_USER = 4

fun main() {
    val trainer = LearnWordTrainer()

    while (true) {
        println(
            """

            МЕНЮ:
        1 - Учить слова
        2 - Статистика
        0 - Выход
    """.trimIndent()
        )
        println("\nВыберите пункт меню \n")
        when (readln().toIntOrNull()) {

            1 -> while (true) {
                val question = trainer.getNextQuestion()
                if (question != null) {
                    if (MAX_LIST_WORD_FOR_USER > question.newListForUser.size) {
                        question.newListForUser += question.learnedWords.shuffled()
                            .take(MAX_LIST_WORD_FOR_USER - question.newListForUser.size)
                    }
                    println("\n${question.wordForUser.engWord.uppercase()}")
                    println("Выберите вариант ответа из списка: \n")
                    question.newListForUser.forEachIndexed { index, el -> println("${index + 1} - ${el.rusWord} ") }
                    println("\n0 - выйти в меню")
                    when (val userChoice = readln().toIntOrNull()) {
                        in 1..MAX_LIST_WORD_FOR_USER -> trainer.checkUserChoice(userChoice)

                        0 -> break

                        else -> println("неправильно ввели число")
                    }
                }
                if (question == null) {
                    println("ВЫ ВЫУЧИЛИ ВСЕ СЛОВА\n")
                    break
                }
            }

            2 -> trainer.getStatistic()

            0 -> break

            else -> println("Вы ввели некорректное значение")
        }
    }
}

data class Word(
    val engWord: String,
    val rusWord: String,
    var correctAnswersCount: Int = 0,
)

data class Question(
    val remainsWord: List<Word>,
    var newListForUser: List<Word>,
    val learnedWords: List<Word>,
    val wordForUser: Word,
)

class LearnWordTrainer() {

    val dictionary = loadDictionary()

    private fun loadDictionary(): List<Word> {
        val dictionaryFile = File("words.txt")
        val dictionary = mutableListOf<Word>()
        val lines = dictionaryFile.readLines()
        for (line in lines) {
            val splitString = line.split("|")
            val word = Word(
                engWord = splitString[0].trim(),
                rusWord = splitString[1].trim(),
                splitString[2].toIntOrNull() ?: 0
            )
            dictionary.add(word)
        }
        return dictionary
    }

    fun saveDictionary(dictionary: List<Word>) {
        val dictionaryFile = File("words.txt")
        dictionaryFile.writeText("")
        dictionary.forEach { dictionaryFile.appendText("${it.engWord}|${it.rusWord}|${it.correctAnswersCount}\n") }
    }

    fun getStatistic() {
        val learnWord = dictionary.filter { it.correctAnswersCount >= LIMIT_OF_LEARNED_WORD }.size
        println(
            "$learnWord из ${dictionary.size} | ${((learnWord.toFloat() / dictionary.size) * 100).roundToInt()}%"
        )
    }

    fun getNextQuestion(): Question? {
        val remainsWord = dictionary.filter { it.correctAnswersCount < LIMIT_OF_LEARNED_WORD }
        val newListForUser = remainsWord.shuffled().take(MAX_LIST_WORD_FOR_USER)
        val learnedWords = dictionary.filter { it.correctAnswersCount >= LIMIT_OF_LEARNED_WORD }
        val wordForUser = newListForUser.random()
        if (remainsWord.isEmpty()) {
            return null
        }
        return Question(
            remainsWord = remainsWord,
            newListForUser = newListForUser,
            learnedWords = learnedWords,
            wordForUser = wordForUser
        )
    }

    fun checkUserChoice(userChoice: Int?) {
        val question2 = LearnWordTrainer()
        val zzz = question2.getNextQuestion()
        val correctAnswerIndex = zzz?.newListForUser?.indexOf(zzz.wordForUser)
        if (correctAnswerIndex != null) {
            if (correctAnswerIndex + 1 == userChoice) {
                println("\u001B[32mПРАВИЛЬНО\u001B[39m")

                zzz.wordForUser.correctAnswersCount++
                question2.saveDictionary(question2.dictionary)
            } else {
                println(
                    "\u001B[31mНЕ ВЕРНО\u001B[39m  Вы выбрали ${
                        (userChoice?.minus(1)
                            ?.let { zzz.newListForUser[it] }?.rusWord)?.uppercase() ?: ""
                    }  Правильный ответ ${(zzz.wordForUser.rusWord).uppercase()}"
                )
            }
        }
    }
}