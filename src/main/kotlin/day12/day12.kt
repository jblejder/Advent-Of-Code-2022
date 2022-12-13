package day12

import AoCTask
import runAoC
import kotlin.math.min

private val testInput = """
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi

""".trimIndent()

private class Data(
    var position: Point = Point(0, 0),
    var goal: Point = Point(0, 0),
    val map: MutableList<MutableList<Int>> = mutableListOf()
)

private data class Point(val x: Int, val y: Int)

private val Point.up: Point
    get() = this.copy(x = x - 1)

private val Point.down: Point
    get() = this.copy(x = x + 1)

private val Point.left: Point
    get() = this.copy(y = y - 1)

private val Point.right: Point
    get() = this.copy(y = y + 1)

private val Point.neighbours: List<Point>
    get() = listOf(up, down, left, right)


fun Char.toElevation() = this.code - 97
fun Int.fromElevation() = this.toChar() + 97

val day12p1: AoCTask = { input: String ->
    println('a'.code - 97)

    val data = input.trim().lines().foldIndexed(Data()) { indexX, acc, next ->
        val line = mutableListOf<Int>()
        next.forEachIndexed { indexY, c ->
            when (c) {
                'S' -> {
                    acc.position = Point(indexX, indexY)
                    line.add('a'.toElevation())
                }
                'E' -> {
                    acc.goal = Point(indexX, indexY)
                    line.add('z'.toElevation())
                }
                else -> {
                    line.add(c.toElevation())
                }
            }
        }
        acc.map.add(line)
        acc
    }
    val maxX = data.map.size
    val maxY = data.map[0].size

    val distances: MutableMap<Point, MutableMap<Point, Int>> = mutableMapOf()

    val unvisited = mutableListOf<Point>()
    val visited = mutableSetOf<Point>()

    fun setDistance(p1: Point, p2: Point, distance: Int) {
        val p1p2 = distances.getOrElse(p1) { mutableMapOf() }
        p1p2[p2] = distance
        distances[p1] = p1p2
    }

    fun getDistance(p1: Point, p2: Point): Int {
        val p1p2 = distances[p1]
        return if (p1p2 == null) Int.MAX_VALUE
        else p1p2[p2] ?: Int.MAX_VALUE
    }

    fun inBounds(position: Point): Boolean {
        return position.x in 0 until maxX
                && position.y in 0 until maxY
    }

    fun canGo(from: Point, to: Point): Boolean {
        return data.map[to.x][to.y] - data.map[from.x][from.y] <= 1
    }

    fun updateNeighboursDistance(point: Point, currentDistanceToStart: Int): Map<Int, Point> {
        return point.neighbours
            .filter { inBounds(it) && canGo(point, it) }.associate {
                val distance = getDistance(data.position, it)
                val newDistance = currentDistanceToStart + 1
                if (newDistance < distance) {
                    setDistance(data.position, it, newDistance)
                    unvisited.add(it)
                }
                distance to point
            }
    }

    fun printMap() {
        for (x in 0 until maxX) {
            for (y in 0 until maxY) {
                val distance = getDistance(data.position, Point(x, y))
                val elevationChar = data.map[x][y].fromElevation()
                if (distance == Int.MAX_VALUE) {
                    print("XX:$elevationChar ")
                } else {
                    print(distance.toString().padStart(2, '0') + ":$elevationChar ")
                }
            }
            println()
        }
    }

    setDistance(data.position, data.position, 0)
    visited.add(data.position)

    var position: Point? = data.position
    while (position != null) {
        visited.add(position)
        updateNeighboursDistance(position, getDistance(data.position, position))
        unvisited.sortBy { getDistance(data.position, it) }
        position = unvisited.removeFirstOrNull()
        println("iteration ${visited.size} - current $position")
        println("unvisited to go $unvisited")

        if (visited.size % 100 == 0) {
//            printMap()
            println()
        }
    }

    getDistance(data.position, data.goal)
}


val day12p2: AoCTask = { input ->
    println('a'.code - 97)

    val data = input.trim().lines().foldIndexed(Data()) { indexX, acc, next ->
        val line = mutableListOf<Int>()
        next.forEachIndexed { indexY, c ->
            when (c) {
                'S' -> {
                    acc.position = Point(indexX, indexY)
                    line.add('a'.toElevation())
                }
                'E' -> {
                    acc.goal = Point(indexX, indexY)
                    line.add('z'.toElevation())
                }
                else -> {
                    line.add(c.toElevation())
                }
            }
        }
        acc.map.add(line)
        acc
    }
    val maxX = data.map.size
    val maxY = data.map[0].size

    var distances: MutableMap<Point, MutableMap<Point, Int>> = mutableMapOf()
    var unvisited = mutableListOf<Point>()
    var visited = mutableSetOf<Point>()

    fun setDistance(p1: Point, p2: Point, distance: Int) {
        val p1p2 = distances.getOrElse(p1) { mutableMapOf() }
        p1p2[p2] = distance
        distances[p1] = p1p2
    }

    fun getDistance(p1: Point, p2: Point): Int {
        val p1p2 = distances[p1]
        return if (p1p2 == null) Int.MAX_VALUE
        else p1p2[p2] ?: Int.MAX_VALUE
    }

    fun inBounds(position: Point): Boolean {
        return position.x in 0 until maxX
                && position.y in 0 until maxY
    }

    fun canGo(from: Point, to: Point): Boolean {
        return data.map[to.x][to.y] - data.map[from.x][from.y] <= 1
    }

    fun updateNeighboursDistance(startPoint: Point, point: Point, currentDistanceToStart: Int): Map<Int, Point> {
        return point.neighbours
            .filter { inBounds(it) && canGo(point, it) }.associate {
                val distance = getDistance(startPoint, it)
                val newDistance = currentDistanceToStart + 1
                if (newDistance < distance) {
                    setDistance(startPoint, it, newDistance)
                    unvisited.add(it)
                }
                distance to point
            }
    }

    val allStartingPositions: List<Point> = data.map
        .mapIndexed { x, ints ->
            ints.mapIndexedNotNull { y, elevation -> if (elevation == 0) Point(x, y) else null }
        }.flatten()
    var lowestDistance = Int.MAX_VALUE

    for (startPoint in allStartingPositions) {
        setDistance(startPoint, startPoint, 0)
        visited.add(startPoint)
        var position: Point? = startPoint
        while (position != null) {
            visited.add(position)
            updateNeighboursDistance(startPoint, position, getDistance(startPoint, position))
            unvisited.sortBy { getDistance(startPoint, it) }
            position = unvisited.removeFirstOrNull()
//            println("iteration ${visited.size} - current $position")
//            println("unvisited to go $unvisited")
        }
        val distance = getDistance(startPoint, data.goal)
        lowestDistance = min(lowestDistance, distance)

        distances = mutableMapOf()
        unvisited = mutableListOf()
        visited = mutableSetOf()
    }

    lowestDistance
}

fun main() = runAoC(12, day12p1, day12p2)
