package telegramBot

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
                val question = trainer.lastQuestion()
                if (question != null) {
                    if (MAX_LIST_WORD_FOR_USER > question.variants.size) {
                        question.variants += question.learnedWords.shuffled()
                            .take(MAX_LIST_WORD_FOR_USER - question.variants.size)
                    }
                    println("\n${question.correctAnswer.engWord.uppercase()}")
                    println("Выберите вариант ответа из списка: \n")
                    question.variants.forEachIndexed { index, el -> println("${index + 1} - ${el.rusWord} ") }
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
                println()                    // TODO здесь вывести

            0 -> break

            else -> println("Вы ввели некорректное значение")
        }
    }
}

