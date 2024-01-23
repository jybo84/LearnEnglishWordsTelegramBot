package TelegramBot

import java.io.File

fun main() {
    val one = File("book.txt")
    one.createNewFile()
    one.writeText("Максим привет ")
    one.appendText("\nНапрягись в феврале собесы")

//    println(one.readText())
//    println(one.readLines())

    val line = one.readLines()
    for (el in line){
        var zzz = el.split(" ")
        val ddddd = Ddddd(zzz[0], zzz[1])
        println(ddddd)
    }
}

data class Ddddd(val first: String, val Second: String)