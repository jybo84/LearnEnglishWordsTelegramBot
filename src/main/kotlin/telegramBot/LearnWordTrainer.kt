package telegramBot

import java.io.File
import kotlin.math.roundToInt

data class Word(
    val engWord: String,
    val rusWord: String,
    var correctAnswersCount: Int = 0,
)

data class Question(
    var variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordTrainer() {

    private var lastQuestion: Question? = null
    private val dictionary = loadDictionary()


    fun getStatistic() {
        val learnWord = dictionary.filter { it.correctAnswersCount >= LIMIT_OF_LEARNED_WORD }.size
//        println(
//            "$learnWord из ${dictionary.size} | ${((learnWord.toFloat() / dictionary.size) * 100).roundToInt()}%"
//        )
        return Statistic()
    }

    fun lastQuestion(): Question? {
        val remainsWord = dictionary.filter { it.correctAnswersCount < LIMIT_OF_LEARNED_WORD }
        if (remainsWord.isEmpty()) {
            return null
        }
//        if (MAX_LIST_WORD_FOR_USER > remainsWord.size) {
//            remainsWord += question.learnedWords.shuffled()
//                .take(MAX_LIST_WORD_FOR_USER - question.variants.size)
//        }
        val newListForUser = remainsWord.shuffled().take(MAX_LIST_WORD_FOR_USER)
        val wordForUser = newListForUser.random()

         lastQuestion = Question(
            variants = newListForUser,
            correctAnswer = wordForUser
        )
        return lastQuestion
    }

    fun checkUserChoice(userChoice: Int?) {
        val correctAnswerIndex =
            lastQuestion()?.variants?.indexOf(lastQuestion()!!.correctAnswer)
        if (correctAnswerIndex != null) {
            if (userChoice != null) {
                if (correctAnswerIndex + 1 == userChoice) {
                    // println("\u001B[32mПРАВИЛЬНО\u001B[39m")

                    lastQuestion()!!.correctAnswer.correctAnswersCount++
                    saveDictionary(dictionary)
                } else {
//                    println(
//                        "\u001B[31mНЕ ВЕРНО\u001B[39m  Вы выбрали ${
//                            (userChoice.minus(1)
//                                .let { lastQuestion()!!.variants[it] }.rusWord).uppercase() ?: "" //TODO так тоже ошибка
//                        }  Правильный ответ ${(lastQuestion()!!.correctAnswer.rusWord).uppercase()}"
//                    )
                }
            }
        }
    }

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

    private fun saveDictionary(dictionary: List<Word>) {
        val dictionaryFile = File("words.txt")
        dictionaryFile.writeText("")
        dictionary.forEach { dictionaryFile.appendText("${it.engWord}|${it.rusWord}|${it.correctAnswersCount}\n") }
    }
}