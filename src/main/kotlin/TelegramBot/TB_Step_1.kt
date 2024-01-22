package TelegramBot

import java.io.File

/*
Задание 2: Воспользуйся readLines(), чтобы достать строки из файла. Распечатай их в цикле
(каждая с новой строки соответственно), чтобы удостовериться, что все работает.
 */
fun main() {

    val text = File("text.txt")
    text.createNewFile()
    text.writeText("hello ")
    text.appendText("Привет ")

    text.appendText("\ndog ")
    text.appendText("собака ")

    text.appendText("\ncat ")
    text.appendText("кошка ")

    println(text.readText())
    println()
    println(text.readLines())
}
