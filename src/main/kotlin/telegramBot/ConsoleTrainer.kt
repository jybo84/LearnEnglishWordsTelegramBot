package telegramBot

data class Word(
    val engWord: String,
    val rusWord: String,
    var correctAnswersCount: Int = 0,
)

fun Question.asConsoleString(): String {
    val variants = this.variants
        .mapIndexed { index: Int, word: Word -> "${index + 1} - ${word.rusWord}" }
        .joinToString("\n")
    return this.correctAnswer.engWord + "\n" + variants + "\n0 - выйти в меню"
}

fun main() {
    val trainer = try {
        LearnWordTrainer(3, 4)
    } catch (e: Exception) {
        println("невозможно загрузить словарь")
        return
    }

    while (true) {

        println(
            """

            МЕНЮ:
        1 - Учить слова
        2 - Статистика
        
        0 - Выход
        
        Выберите пункт меню
    """.trimIndent()
        )

        when (readln().toIntOrNull()) {

            1 -> while (true) {
                val question = trainer.getNextQuestion()
                if (question == null) {
                    println("ВЫ ВЫУЧИЛИ ВСЕ СЛОВА\n")
                    break
                } else {
                    println(question.asConsoleString())

                    val userAnswerInput = readln().toIntOrNull()
                    if (userAnswerInput == 0) break

                    if (trainer.checkAnswer(userAnswerInput?.minus(1))) {
                        println("Правильно")
                    } else {
                        println("Неправильно! ${question.correctAnswer.engWord} - это ${question.correctAnswer.rusWord}")
                    }
                }
            }

            2 -> {
                val statistic = trainer.getStatistic()
                println("Выучено ${statistic.learned} из ${statistic.total} слов | ${statistic.percent}")
            }

            0 -> break

            else -> println("Вы ввели некорректное значение")
        }
    }
}



