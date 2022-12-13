package day10

import AoCTask
import runAoC

private val testInput = """
    addx 15
    addx -11
    addx 6
    addx -3
    addx 5
    addx -1
    addx -8
    addx 13
    addx 4
    noop
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx -35
    addx 1
    addx 24
    addx -19
    addx 1
    addx 16
    addx -11
    noop
    noop
    addx 21
    addx -15
    noop
    noop
    addx -3
    addx 9
    addx 1
    addx -3
    addx 8
    addx 1
    addx 5
    noop
    noop
    noop
    noop
    noop
    addx -36
    noop
    addx 1
    addx 7
    noop
    noop
    noop
    addx 2
    addx 6
    noop
    noop
    noop
    noop
    noop
    addx 1
    noop
    noop
    addx 7
    addx 1
    noop
    addx -13
    addx 13
    addx 7
    noop
    addx 1
    addx -33
    noop
    noop
    noop
    addx 2
    noop
    noop
    noop
    addx 8
    noop
    addx -1
    addx 2
    addx 1
    noop
    addx 17
    addx -9
    addx 1
    addx 1
    addx -3
    addx 11
    noop
    noop
    addx 1
    noop
    addx 1
    noop
    noop
    addx -13
    addx -19
    addx 1
    addx 3
    addx 26
    addx -30
    addx 12
    addx -1
    addx 3
    addx 1
    noop
    noop
    noop
    addx -9
    addx 18
    addx 1
    addx 2
    noop
    noop
    addx 9
    noop
    noop
    noop
    addx -1
    addx 2
    addx -37
    addx 1
    addx 3
    noop
    addx 15
    addx -21
    addx 22
    addx -6
    addx 1
    noop
    addx 2
    addx 1
    noop
    addx -10
    noop
    noop
    addx 20
    addx 1
    addx 2
    addx 2
    addx -6
    addx -11
    noop
    noop
    noop

""".trimIndent()


private val day10p1: AoCTask = { input: String ->
    var sum = 0
    var clock = 0
    var reqX = 1

    val important = setOf(20, 60, 100, 140, 180, 220)

    fun checkState(clock: Int) {
        if (important.contains(clock)) {
            println("c: $clock val: $reqX str: ${clock * reqX}")
            sum += clock * reqX
        }
    }

    input.lines().filter { it.isNotEmpty() }.forEach {
        if (it.startsWith("noop")) {
            clock += 1
            checkState(clock)
        } else {
            clock += 1
            checkState(clock)
            clock += 1
            checkState(clock)

            val input = it.split(" ")
            reqX += input[1].toInt()
        }
    }

    sum
}

private val day10p2: AoCTask = { input ->
    val screen = mutableListOf<Char>()
    var sum = 0
    var clock = -1
    var reqX = 1


    val important = setOf(20, 60, 100, 140, 180, 220)

    fun checkState(clock: Int) {
//        if (important.contains(clock)) {
        println("c- $clock val: $reqX str: ${clock * reqX}")
        sum += clock * reqX
//        }
    }

    fun draw(clock: Int) {
        checkState(clock)
        val pos = clock % 40
        if (pos >= reqX - 1 && pos <= reqX + 1) {
            screen.add(clock % (40 * 6), '#')
        } else {
            screen.add(clock % (40 * 6), '.')
        }
    }

    input.lines().filter { it.isNotEmpty() }.forEach {
        if (it.startsWith("noop")) {
            clock += 1
            draw(clock)
        } else {
            clock += 1
            draw(clock)
            clock += 1
            draw(clock)

            val input = it.split(" ")
            reqX += input[1].toInt()
        }
    }

    screen.chunked(40).forEach {
        it.forEach { print(it) }
        println()
    }

    sum
}

fun main() = runAoC(10, day10p1, day10p2)
