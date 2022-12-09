package day5

import AoCTask
import runAoC


val day5p1: AoCTask = { input: String ->
    var stacks = mutableMapOf<Int, List<Char>>()
    var headerSize = 0
    val stacksRaw = input.lines().takeWhile {
        headerSize++
        it.isNotEmpty()
    }.dropLast(1)

    stacksRaw.fold(stacks) { acc, line ->
        line.chunked(4)
            .foldIndexed(acc) { i, acc, it ->
                val item = it.trim()
                when {
                    item.isNotEmpty() -> {
                        val list = acc.getOrElse(i) { listOf() }
                        acc[i] = list + item[1]
                    }
                }
                acc
            }
        acc
    }
    stacks = stacks.map { it.key to it.value.reversed() }.toMap().toMutableMap()
    input.lines()
        .drop(headerSize)
        .filter { it.isNotEmpty() }
        .forEach {
            val numbers = it.split(" ").mapNotNull { it.toIntOrNull() }
            repeat(numbers[0]) {
                val from = stacks[numbers[1] - 1]!!
                val to = stacks[numbers[2] - 1]!!
                stacks[numbers[2] - 1] = to + from.last()
                stacks[numbers[1] - 1] = from.dropLast(1)
            }
        }

    stacks.keys.sorted().map { stacks[it]!!.last() }.joinToString("")
}

val day5p2: AoCTask = { input ->
    var stacks = mutableMapOf<Int, List<Char>>()
    var headerSize = 0
    val stacksRaw = input.lines().takeWhile {
        headerSize++
        it.isNotEmpty()
    }.dropLast(1)

    stacksRaw.fold(stacks) { acc, line ->
        line.chunked(4)
            .foldIndexed(acc) { i, acc, it ->
                val item = it.trim()
                when {
                    item.isNotEmpty() -> {
                        val list = acc.getOrElse(i) { listOf() }
                        acc[i] = list + item[1]
                    }
                }
                acc
            }
        acc
    }
    stacks = stacks.map { it.key to it.value.reversed() }.toMap().toMutableMap()
    input.lines()
        .drop(headerSize)
        .filter { it.isNotEmpty() }
        .forEach {
            val numbers = it.split(" ").mapNotNull { it.toIntOrNull() }

            val from = stacks[numbers[1] - 1]!!
            val to = stacks[numbers[2] - 1]!!
            stacks[numbers[2] - 1] = to + from.takeLast(numbers[0])
            stacks[numbers[1] - 1] = from.dropLast(numbers[0])

        }

    stacks.keys.sorted().map { stacks[it]!!.last() }.joinToString("")
}

fun main() = runAoC(5, day5p1, day5p2)
