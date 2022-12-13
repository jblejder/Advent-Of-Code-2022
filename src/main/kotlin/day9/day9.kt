package day9

import AoCTask
import runAoC
import kotlin.math.abs

private val testInput = """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
""".trimIndent()
private fun vector(txt: String, lenght: Int): Point {
    return when (txt) {
        "R" -> Point(lenght, 0)
        "U" -> Point(0, lenght)
        "L" -> Point(-lenght, 0)
        "D" -> Point(0, -lenght)
        else -> throw java.lang.RuntimeException("nope")
    }
}

private data class Point(val x: Int, val y: Int)

private operator fun Point.plus(other: Point): Point =
    Point(this.x + other.x, this.y + other.y)

private operator fun Point.minus(other: Point): Point =
    Point(this.x - other.x, this.y - other.y)

private fun Point.norm(): Point {
    fun Int.norm(): Int {
        return if (this == 0) 0
        else this / abs(this)
    }
    return Point(x.norm(), y.norm())
}

private val day9p1: AoCTask = { input: String ->
    var head = Point(0, 0)
    var tail = Point(0, 0)
    val visited = mutableSetOf(tail)

    input.lines().filter { it.isNotEmpty() }
        .forEach {
            val split = it.split(" ")
            repeat(split[1].toInt()) {
                val newHead = head + vector(split[0], 1)
                val diff = newHead - tail
                if (abs(diff.x) > 1 || abs(diff.y) > 1) {
                    tail += diff.norm()
                    visited.add(tail)
                }
                head = newHead
                println("head: $head - tail: $tail - diff: $diff")
            }
        }
    visited.size
}


private fun print(set: Set<Point>) {
    val minX = set.minOf { it.x }
    val maxX = set.maxOf { it.x }
    val minY = set.minOf { it.y }
    val maxY = set.maxOf { it.y }

    for (x in minX until maxX) {
        for (y in minY until maxY) {
            if (set.contains(Point(x, y))) {
                print("X")
            } else {
                print(".")
            }
        }
        println()
    }


}

private val testInput2 = """
    R 5
    U 8
    L 8
    D 3
    R 17
    D 10
    L 25
    U 20
""".trimIndent()

private val day9p2: AoCTask = { input ->
    var snake = mutableListOf<Point>()
    repeat(10) { snake.add(Point(0, 0)) }
    val visited = mutableSetOf(snake.last())

    input.lines().filter { it.isNotEmpty() }
        .forEach {
            val split = it.split(" ")
            repeat(split[1].toInt()) {
                val newSnake = mutableListOf<Point>()
                newSnake.add(snake[0] + vector(split[0], 1))
                for (i in 1 until snake.size) {
                    val diff = newSnake[i - 1] - snake[i]
                    if (abs(diff.x) > 1 || abs(diff.y) > 1) {
                        newSnake.add(snake[i] + diff.norm())
                    } else {
                        newSnake.add(snake[i])
                    }
                }
                visited.add(newSnake.last())
                snake = newSnake
            }
        }
    visited.size
}

fun main() = runAoC(9, day9p1, day9p2)
