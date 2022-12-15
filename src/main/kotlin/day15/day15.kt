package day15

import AoCTask
import runAoC
import kotlin.math.abs

val testInput = """
    Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    Sensor at x=9, y=16: closest beacon is at x=10, y=16
    Sensor at x=13, y=2: closest beacon is at x=15, y=3
    Sensor at x=12, y=14: closest beacon is at x=10, y=16
    Sensor at x=10, y=20: closest beacon is at x=10, y=16
    Sensor at x=14, y=17: closest beacon is at x=10, y=16
    Sensor at x=8, y=7: closest beacon is at x=2, y=10
    Sensor at x=2, y=0: closest beacon is at x=2, y=10
    Sensor at x=0, y=11: closest beacon is at x=2, y=10
    Sensor at x=20, y=14: closest beacon is at x=25, y=17
    Sensor at x=17, y=20: closest beacon is at x=21, y=22
    Sensor at x=16, y=7: closest beacon is at x=15, y=3
    Sensor at x=14, y=3: closest beacon is at x=15, y=3
    Sensor at x=20, y=1: closest beacon is at x=15, y=3
""".trimIndent()

data class DataPoint(
    var excluded: Boolean = false,
    var sensor: Boolean = false,
    var beacon: Boolean = false
)

class Map() {
    val objects = mutableMapOf<Int, MutableMap<Int, DataPoint>>()

    val minX: Int
        get() = objects.keys.minOf { it }
    val maxX: Int
        get() = objects.keys.maxOf { it }
    val maxY: Int
        get() = objects.values.maxOf { it.keys.maxOf { it } }
    val minY: Int
        get() = objects.values.minOf { it.keys.minOf { it } }

    fun get(x: Int, y: Int): DataPoint {
        return objects[x]?.let { it[y] } ?: DataPoint()
    }

    fun putObject(x: Int, y: Int, item: DataPoint) {
        val row = objects.getOrDefault(x, mutableMapOf())
        row[y] = item
        objects[x] = row
    }

    fun update(x: Int, y: Int, block: (DataPoint) -> DataPoint) {
        val row = objects.getOrDefault(x, mutableMapOf())
        val dataPoint = row.getOrDefault(y, DataPoint())
        row[y] = block(dataPoint)
        objects[x] = row
    }

    fun remove(x: Int, y: Int) {
        val row = objects.getOrDefault(x, mutableMapOf())
        row.remove(y)
        objects[x] = row
    }

    fun printMap() {
        val minX = this.minX
        val maxX = this.maxX
        val minY = this.minY
        val maxY = this.maxY
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val item = get(x, y)
                when {
                    item.beacon && item.sensor -> print("Z")
                    item.beacon -> print("B")
                    item.sensor -> print("S")
                    item.excluded -> print("#")
                    else -> print(".")
                }
            }
            println()
        }
    }
}

fun distance(sX: Int, sY: Int, bX: Int, bY: Int): Int {
    return abs(bX - sX) + abs(bY - sY)
}

fun updateInRange(map: Map, x: Int, y: Int, distance: Int, block: (DataPoint) -> DataPoint) {
    for (i in x..(x + distance)) {
        for (j in y..(y + distance - abs(i - x))) {
            map.update(i, j, block)
        }
    }

    for (i in x downTo (x - distance)) {
        for (j in y..(y + distance - abs(i - x))) {
            map.update(i, j, block)
        }
    }

    for (i in x downTo (x - distance)) {
        for (j in y downTo (y - distance + abs(i - x))) {
            map.update(i, j, block)
        }
    }

    for (i in x..(x + distance)) {
        for (j in y downTo (y - distance + abs(i - x))) {
            map.update(i, j, block)
        }
    }
}

val day15p1: AoCTask = { input: String ->
    val map = Map()
    input.trim().lines().forEach { line ->
        val values = line.split(" ").mapNotNull {
            it.replace("x=", "")
                .replace("y=", "")
                .replace(",", "")
                .replace(":", "")
                .toIntOrNull()
        }
        val sX = values[0]
        val sY = values[1]
        val bX = values[2]
        val bY = values[3]

        map.update(sX, sY) { it.copy(sensor = true) }
        map.update(bX, bY) { it.copy(beacon = true) }
        val distance = distance(sX, sY, bX, bY)
        updateInRange(map, sX, sY, distance) { it.copy(excluded = true) }
    }
    map.printMap()

    var count = 0
    for (x in map.minX..map.maxX) {
        val item = map.get(x, 10)
        if (!item.beacon && item.excluded) {
            count++
        }
    }
    count
}


val day15p2: AoCTask = { input ->
    ""
}

fun main() = runAoC(15, day15p1, day15p2)