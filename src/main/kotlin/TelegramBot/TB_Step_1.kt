package TelegramBot

import java.io.File

fun main() {

    val text = File("text.txt")
    text.createNewFile()
    text.writeText("hello привет")
    text.appendText("\n")

    text.appendText("dog собака ")
    text.appendText("\n")

    text.appendText("cat кошка")


    println(text.readText())
    println()
    println(text.readLines())

    val lines: List<String> = text.readLines()
    for(el in lines)
        println(el)
}