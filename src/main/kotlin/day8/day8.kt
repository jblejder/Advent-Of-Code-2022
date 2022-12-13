package day8

import AoCTask
import runAoC


private val testInput = """
    30373
    25512
    65332
    33549
    35390
""".trimIndent()


private val day8p1: AoCTask = { input: String ->
    val trees = mutableListOf<MutableList<Int>>()

    input.lines().filter { it.isNotEmpty() }
        .forEachIndexed { i, line ->
            trees.add(mutableListOf())
            line.forEachIndexed { j, char ->
                trees[i].add(char.digitToInt())
            }
        }

    var visible = 0
    for ((i, line) in trees.withIndex()) {
        for ((j, tree) in line.withIndex()) {
            if (isVisible(trees, i, j)) {
                visible++
            }
        }
    }

    visible
}

private fun isVisible(trees: MutableList<MutableList<Int>>, x: Int, y: Int): Boolean {
    if (x == 0 || y == 0 || x == trees.size - 1 || y == trees[x].size - 1) return true

    var isVisible = true
    for (i in 0 until x) {
        if (trees[i][y] >= trees[x][y]) {
            isVisible = false
        }
    }
    if (isVisible) return true

    isVisible = true
    for (i in x + 1 until trees.size) {
        if (trees[i][y] >= trees[x][y]) {
            isVisible = false
        }
    }
    if (isVisible) return true

    isVisible = true
    for (i in 0 until y) {
        if (trees[x][i] >= trees[x][y]) {
            isVisible = false
        }
    }
    if (isVisible) return true

    isVisible = true
    for (i in y + 1 until trees[x].size) {
        if (trees[x][i] >= trees[x][y]) {
            isVisible = false
        }
    }
    if (isVisible) return true

    return false
}


private val day8p2: AoCTask = { input ->
    val trees = mutableListOf<MutableList<Int>>()

    input.lines().filter { it.isNotEmpty() }
        .forEachIndexed { i, line ->
            trees.add(mutableListOf())
            line.forEachIndexed { j, char ->
                trees[i].add(char.digitToInt())
            }
        }

    var max: Long = 0
    for ((i, line) in trees.withIndex()) {
        for ((j, tree) in line.withIndex()) {
            val score = score(trees, i, j)
            max = maxOf(max, score)
        }
    }

    max
}

private fun score(trees: MutableList<MutableList<Int>>, x: Int, y: Int): Long {

    if (x == 1 && y == 2) {
        println()
    }
    var topScore = 0L
    for (i in (0 until x).reversed()) {
        topScore++
        if (trees[i][y] >= trees[x][y]) {
            break
        }
    }

    var bottomScore = 0L
    for (i in x + 1 until trees.size) {
        bottomScore++
        if (trees[i][y] >= trees[x][y]) {
            break;
        }
    }

    var leftScore = 0L
    for (i in (0 until y).reversed()) {
        leftScore++
        if (trees[x][i] >= trees[x][y]) {
            break
        }
    }

    var rightScore = 0L
    for (i in y + 1 until trees[x].size) {
        rightScore++
        if (trees[x][i] >= trees[x][y]) {
            break;
        }
    }

    return leftScore * rightScore * bottomScore * topScore
}

fun main() = runAoC(8, day8p1, day8p2)
