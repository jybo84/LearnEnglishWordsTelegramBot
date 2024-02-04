package telegramBot

import java.io.File

const val LIMIT_OF_LEARNED_WORD = 1
const val MAX_LIST_WORD_FOR_USER = 4

fun main() {
    val trainer = LearnWordsTrainer()

    while (true) {
        println(
            """

            МЕНЮ:
        1 - Учить слова
        2 - Статистика
        
        0 - Выход
    """.trimIndent()
        )
        when (readln().toIntOrNull()) {
            1 -> {
                while (true) {
                    val question = trainer.getNextQuestion()
                    if (question == null) {
                        println("Все слова выучены")
                        break
                    } else {
                        println(question.questionToString())
                        val userAnswerInput = readln().toIntOrNull()
                        if (userAnswerInput == 0) break
                        if (trainer.checkAnswer(userAnswerInput?.minus(1))) {
                            println("Правильно\n")
                        } else {
                            println("Неправильно ${question.correctAnswer.questionsWord} -это ${question.correctAnswer}")
                        }
                    }
                }
            }

            2 -> {
                val statistic = trainer.getStatistics()
                println("Выучено ${statistic.learned} из ${statistic.total} слов | ${statistic.percent}%")
            }

            0 -> break

            else -> println("Введите 1, 2 или 0")
        }
    }
}

data class Word(
    val questionsWord: String,
    val translate: String,
    var correctAnswerCount: Int = 0,
)

fun Question.questionToString(): String {
    val variants = this.variants
        .mapIndexed { index: Int, word: Word -> "${index + 1} - ${word.translate}" }
        .joinToString(separator = "\n")
    return this.correctAnswer.questionsWord + "\n" + variants + "\n0 - выйти в меню"
}

data class Statistics(
    val learned: Int,
    val total: Int,
    val percent: Int,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)


class LearnWordsTrainer {
    private var question: Question? = null
    private var dictionary = loadDictionary()

    private fun loadDictionary(): List<Word> {
        val dictionary = mutableListOf<Word>()
        val wordFile = File("words.txt")
        wordFile.readLines().forEach {
            val splitLine = it.split("|")
            dictionary.add(Word(splitLine[0], splitLine[1], splitLine[2].toIntOrNull() ?: 0))
        }
        return dictionary
    }

    private fun saveDictionary(words: List<Word>) {
        val wordsFile = File("words.txt")
        wordsFile.writeText("")
        for (word in words) {
            wordsFile.appendText("${word.questionsWord}| ${word.translate}|${word.correctAnswerCount}")
        }
    }

    fun getStatistics(): Statistics {
        val learned = dictionary.filter { it.correctAnswerCount >= LIMIT_OF_LEARNED_WORD }.size
        val total = dictionary.size
        val percent = learned * 100 / total
        return Statistics(learned, total, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswerCount < LIMIT_OF_LEARNED_WORD }
        if (notLearnedList.isEmpty()) return null
        val questionWords = notLearnedList.shuffled().take(MAX_LIST_WORD_FOR_USER)
        val correctAnswer = questionWords.random()
        question = Question(
            variants = questionWords,
            correctAnswer = correctAnswer,
        )
        return question
    }

    fun checkAnswer(userAnswerIndex: Int?): Boolean {
        return question?.let {
            val correctAnswerId = it.variants.indexOf(it.correctAnswer)
            if (correctAnswerId == userAnswerIndex) {
                it.correctAnswer.correctAnswerCount++
                saveDictionary(dictionary)
                true
            } else {
                false
            }
        } ?: false
    }
}


/*
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
                trainer.getNextQuestion()
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

class LearnWordTrainer() {

    private val dictionary = loadDictionary()

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

    fun getStatistic() {
        val learnWord = dictionary.filter { it.correctAnswersCount >= LIMIT_OF_LEARNED_WORD }.size
        println(
            "$learnWord из ${dictionary.size} | ${((learnWord.toFloat() / dictionary.size) * 100).roundToInt()}%"
        )
    }

    fun getNextQuestion() {
        while (true) {
            val remainsWord = dictionary.filter { it.correctAnswersCount < LIMIT_OF_LEARNED_WORD }
            if (remainsWord.isEmpty()) {
                println("ВЫ ВЫУЧИЛИ ВСЕ СЛОВА\n")
                break
            }
            var newListForUser = remainsWord.shuffled().take(MAX_LIST_WORD_FOR_USER)
            val wordForUser = newListForUser.random()

            if (MAX_LIST_WORD_FOR_USER > newListForUser.size) {
                val learnedWords = dictionary.filter { it.correctAnswersCount >= LIMIT_OF_LEARNED_WORD }
                newListForUser =
                    newListForUser + learnedWords.shuffled().take(MAX_LIST_WORD_FOR_USER - newListForUser.size)
            }
            println("\n${wordForUser.engWord.uppercase()}")
            println("Выберите вариант ответа из списка: \n")
            newListForUser.forEachIndexed { index, el -> println("${index + 1} - ${el.rusWord} ") }

            println("\n0 - выйти в меню")

            when (val userChoice = readln().toIntOrNull()) {
                in 1..MAX_LIST_WORD_FOR_USER -> {
                    val correctAnswerIndex = newListForUser.indexOf(wordForUser)
                    if (correctAnswerIndex + 1 == userChoice) {
                        println("\u001B[32mПРАВИЛЬНО\u001B[39m")
                        wordForUser.correctAnswersCount++
                        saveDictionary(dictionary)
                    } else {
                        println(
                            "\u001B[31mНЕ ВЕРНО\u001B[39m  Вы выбрали ${
                                (userChoice?.minus(1)?.let { newListForUser[it] }?.rusWord)?.uppercase() ?: ""
                            }  Правильный ответ ${(wordForUser.rusWord).uppercase()}"
                        )
                    }
                }

                0 -> break

                else -> println("неправильно ввели число")
            }
        }
    }
}
 */

