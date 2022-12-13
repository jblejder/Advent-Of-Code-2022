package day6

import AoCTask
import runAoC


private val day6p1: AoCTask = { input: String ->
    val data = mutableListOf<Char>()
    var size = 0
    input.forEach {
        data.add(it)
        if (data.takeLast(4).toSet().size == 4) {
            size = data.size
            return@forEach
        }
    }
    size
}

private val day6p2: AoCTask = { input ->
    val data = mutableListOf<Char>()
    var size = 0
    input.forEach {
        data.add(it)
        if (size == 0 && data.takeLast(14).toSet().size == 14) {
            size = data.size
            println(data.takeLast(14) + " " + size)
            return@forEach
        }
    }
    size
}

fun main() = runAoC(6, day6p1, day6p2)
