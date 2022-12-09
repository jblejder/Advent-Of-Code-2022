package day2

import AoCTask
import runAoC

val scores = mapOf(
    Game.Rock to 1,
    Game.Paper to 2,
    Game.Scissors to 3
)

enum class Game {
    Rock, Paper, Scissors
}

val mapO = mapOf(
    "A" to Game.Rock,
    "B" to Game.Paper,
    "C" to Game.Scissors,
)

val mapM = mapOf(
    "X" to Game.Rock,
    "Y" to Game.Paper,
    "Z" to Game.Scissors
)

fun itBeats(a: Game, b: Game): Int {
    return when {
        a == b -> 3
        a == Game.Rock && b == Game.Paper -> 6
        a == Game.Paper && b == Game.Scissors -> 6
        a == Game.Scissors && b == Game.Rock -> 6
        else -> 0
    }
}

fun whatToPick(a: Game, b: String): Game {
    return when (b) {
        "Y" -> a
        "Z" -> {
            when (a) {
                Game.Rock -> Game.Paper
                Game.Paper -> Game.Scissors
                Game.Scissors -> Game.Rock
            }
        }
        "X" -> {
            when (a) {
                Game.Rock -> Game.Scissors
                Game.Paper -> Game.Rock
                Game.Scissors -> Game.Paper
            }
        }
        else -> {
            throw java.lang.RuntimeException("PANIC!")
        }
    }
}

val day2p1: AoCTask = { input: String ->
    input.lines()
        .filter { it.isNotEmpty() }
        .fold(0) { acc, line ->
            val split = line.split(" ")
            val op = mapO[split[0]]!!
            val me = mapM[split[1]]!!
            val useScore = scores[me]!!
            val winScore = itBeats(op, me)

            acc + useScore + winScore
        }
}

val day2p2: AoCTask = { input ->
    input.lines()
        .filter { it.isNotEmpty() }
        .fold(0) { acc, line ->
            val split = line.split(" ")
            val op = mapO[split[0]]!!
            val me = whatToPick(op, split[1])
            val useScore = scores[me]!!
            val winScore = itBeats(op, me)

            acc + useScore + winScore
        }
}

fun main() = runAoC(2, day2p1, day2p2)