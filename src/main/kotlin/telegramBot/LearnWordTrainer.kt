package telegramBot

import java.io.File

const val LIMIT_OF_LEARNED_WORD = 1
const val MAX_LIST_WORD_FOR_USER = 4

data class Statistic(
    val learned: Int,
    val total: Int,
    val percent: Int
)

data class Question(
    var variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordTrainer() {

    private var lastQuestion: Question? = null
    private val dictionary = loadDictionary()

    fun getStatistic(): Statistic {
        val learned = dictionary.filter { it.correctAnswersCount >= LIMIT_OF_LEARNED_WORD }.size //TODO1645
        val total = dictionary.size
        val percent = learned * 100 / total
        return Statistic(learned, total, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < MAX_LIST_WORD_FOR_USER }//TODO1603
        if (notLearnedList.isEmpty()) return null
        val questionWords = notLearnedList.take(4).shuffled()
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


