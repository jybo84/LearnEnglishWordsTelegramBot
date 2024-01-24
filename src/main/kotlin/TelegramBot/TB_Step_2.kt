package TelegramBot

import java.io.File

fun main() {

    val text = File("text.txt")
    text.createNewFile()
    text.writeText("hello | привет | 0")
    text.appendText("\ndog | собака | 0")
    text.appendText("\ncat | кошка | 0")


    val tempList = mutableListOf<Word>()

    val lines = text.readLines()
    for (el in lines) {
        val splitString = el.split(" |")
        val word = Word( splitString[0], splitString[1], correctAnswersCount = 2)
        tempList.add(word)
    }

   val dictionary =  tempList.groupingBy { it }.eachCount()
   dictionary.forEach { println(it)  }
}

data class Word(val engWord: String, val rusWord: String?, val correctAnswersCount: Int = 0)

