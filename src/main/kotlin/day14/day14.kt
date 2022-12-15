package day14

import AoCTask
import runAoC
import java.lang.Integer.max
import kotlin.math.min

private val testInput = """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
""".trimIndent()


enum class Item {
    Rock, Air, Sand
}

private class Map() {

    val objects = mutableMapOf<Int, MutableMap<Int, Item>>()

    val minX: Int
        get() = objects.keys.minOf { it }
    val maxX: Int
        get() = objects.keys.maxOf { it }
    val maxY: Int
        get() = objects.values.maxOf { it.keys.maxOf { it } }

    fun get(x: Int, y: Int): Item {
        return objects[x]?.let { it[y] } ?: Item.Air
    }

    fun putObject(x: Int, y: Int, item: Item): Boolean {
        val row = objects.getOrDefault(x, mutableMapOf())
        val itemInSpace = row.getOrDefault(y, Item.Air)
        return if (itemInSpace == Item.Air) {
            row[y] = item
            objects[x] = row
            true
        } else false
    }

    fun remove(x: Int, y: Int) {
        val row = objects.getOrDefault(x, mutableMapOf())
        row.remove(y)
        objects[x] = row
    }

    fun freeFall(x: Int, y: Int): Pair<Int, Int> {
        val itemToFall = get(x, y)
        if (itemToFall == Item.Air || itemToFall == Item.Rock) {
            throw java.lang.RuntimeException("can't move air or rock, $x:$y")
        }
        val down = get(x, y + 1)
        if (down == Item.Air) {
            remove(x, y)
            putObject(x, y + 1, Item.Sand)
            return x to y + 1
        }
        val downLeft = get(x - 1, y + 1)
        if (downLeft == Item.Air) {
            remove(x, y)
            putObject(x - 1, y + 1, Item.Sand)
            return x - 1 to y + 1
        }
        val downRight = get(x + 1, y + 1)
        if (downRight == Item.Air) {
            remove(x, y)
            putObject(x + 1, y + 1, Item.Sand)
            return x + 1 to y + 1
        }
        return x to y
    }

    fun printMap() {
        val minX = this.minX
        val maxX = this.maxX
        val maxY = this.maxY
        for (y in 0..maxY) {
            for (x in minX..maxX) {
                when (get(x, y)) {
                    Item.Rock -> print("#")
                    Item.Air -> print(".")
                    Item.Sand -> print("o")
                }
            }
            println()
        }
    }
}

val day14p1: AoCTask = { input: String ->
    val map = input.trim().lines().fold(Map()) { acc, next ->
        next.split(" -> ").windowed(2).forEach { window ->
            val start = window[0].split(",")
            val startX = start[0].toInt()
            val startY = start[1].toInt()
            val end = window[1].split(",")
            val endX = end[0].toInt()
            val endY = end[1].toInt()

            val sX = min(startX, endX)
            val eX = max(startX, endX)
            val sY = min(startY, endY)
            val eY = max(startY, endY)

            for (x in sX..eX) {
                for (y in sY..eY) {
                    acc.putObject(x, y, Item.Rock)
                }
            }
        }

        acc
    }

    val maxY = map.maxY
    var fellToInfinity = false
    var sandPieces = 0

    while (!fellToInfinity) {
        var loc = 0 to 0
        var newLoc: Pair<Int, Int> = 500 to 0
        map.putObject(newLoc.first, newLoc.second, Item.Sand)
        while (!fellToInfinity && loc != newLoc) {
            loc = newLoc
            newLoc = map.freeFall(loc.first, loc.second)
            if (newLoc.second > maxY) {
                fellToInfinity = true
            }
        }
        if (!fellToInfinity) {
            sandPieces++
        }
    }

    map.printMap()
    sandPieces
}

val day14p2: AoCTask = { input: String ->
    val map = input.trim().lines().fold(Map()) { acc, next ->
        next.split(" -> ").windowed(2).forEach { window ->
            val start = window[0].split(",")
            val startX = start[0].toInt()
            val startY = start[1].toInt()
            val end = window[1].split(",")
            val endX = end[0].toInt()
            val endY = end[1].toInt()

            val sX = min(startX, endX)
            val eX = max(startX, endX)
            val sY = min(startY, endY)
            val eY = max(startY, endY)

            for (x in sX..eX) {
                for (y in sY..eY) {
                    acc.putObject(x, y, Item.Rock)
                }
            }
        }

        acc
    }

    val maxY = map.maxY
    var sandPieces = 0
    var full = false

    while (!full) {
        var reachedBottom = false
        var loc = 0 to 0
        var newLoc: Pair<Int, Int> = 500 to 0
        full = !map.putObject(newLoc.first, newLoc.second, Item.Sand)
        while (!reachedBottom && loc != newLoc) {
            loc = newLoc
            newLoc = map.freeFall(loc.first, loc.second)
            if (newLoc.second == maxY + 1) {
                reachedBottom = true
            }
        }
        if(!full) {
            sandPieces++
        }
    }

    map.printMap()
    sandPieces
}

fun main() = runAoC(14, day14p1, day14p2)
