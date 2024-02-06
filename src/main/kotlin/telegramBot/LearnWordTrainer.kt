package telegramBot

import java.io.File

data class Statistic(
    val learned: Int,
    val total: Int,
    val percent: Int
)

data class Question(
    var variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordTrainer(
    private val leanedAnswerCount: Int = 1,
    private val countOfQuestionWords: Int = 4
) {

    private var lastQuestion: Question? = null
    private val dictionary = loadDictionary()

    fun getStatistic(): Statistic {
        val learned = dictionary.filter { it.correctAnswersCount >= leanedAnswerCount }.size
        val total = dictionary.size
        val percent = learned * 100 / total
        return Statistic(learned, total, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < leanedAnswerCount }
        if (notLearnedList.isEmpty()) return null
        val questionWords = if (notLearnedList.size < countOfQuestionWords) {
            val learnedList = dictionary.filter { it.correctAnswersCount >= leanedAnswerCount }.shuffled()
            notLearnedList.shuffled()
                .take(countOfQuestionWords) + learnedList.take(countOfQuestionWords - notLearnedList.size)
        } else {
            notLearnedList.shuffled().take(countOfQuestionWords)
        }.shuffled()

        val correctAnswer = questionWords.random()

        lastQuestion = Question(
            variants = questionWords,
            correctAnswer = correctAnswer
        )
        return lastQuestion
    }

    fun checkAnswer(userAnswerIndex: Int?): Boolean {
        return lastQuestion?.let {
            val correctAnswerId = it.variants.indexOf(it.correctAnswer)
            if (correctAnswerId == userAnswerIndex) {
                it.correctAnswer.correctAnswersCount++
                saveDictionary(dictionary)
                true
            } else {
                false
            }
        } ?: false
    }

    private fun loadDictionary(): List<Word> {
        try {
            val dictionary = mutableListOf<Word>()
            val dictionaryFile = File("words.txt")
            dictionaryFile.readLines().forEach {
                val splitString = it.split("|")
                dictionary.add(Word(splitString[0], splitString[1], splitString[2].toIntOrNull() ?: 0))
            }
            return dictionary
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalStateException("некорректный файл")
        }
    }

    private fun saveDictionary(dictionary: List<Word>) {
        val dictionaryFile = File("words.txt")
        dictionaryFile.writeText("")
        dictionary.forEach { dictionaryFile.appendText("${it.engWord}|${it.rusWord}|${it.correctAnswersCount}\n") }
    }
}


