package TelegramBot

import java.io.File

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