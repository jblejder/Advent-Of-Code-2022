package day15.trytwo

import AoCTask
import runAoC
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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

data class Sensor(val point: Point, val range: Int)
data class Point(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x,$y)"
    }
}

fun distance(a: Point, b: Point): Int {
    return distance(a.x, a.y, b.x, b.y)
}

fun distance(sX: Int, sY: Int, bX: Int, bY: Int): Int {
    return abs(bX - sX) + abs(bY - sY)
}

fun readInput(input: String): Pair<List<Sensor>, List<Point>> {
    val beacons = mutableListOf<Point>()
    val sensors = mutableListOf<Sensor>()
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


        val distance = distance(sX, sY, bX, bY)
        sensors.add(Sensor(Point(sX, sY), distance))
        beacons.add(Point(bX, bY))
    }
    return sensors to beacons
}

val day15p1: AoCTask = { input: String ->
    val (sensors, beacons) = readInput(input)
    val y = 2000000
    val taken = sensors.count { it.point.y == y }
    coverage(y, sensors) - taken
}

fun coverage(slice: Int, sensors: List<Sensor>): Int {
    val result = sensors.mapIndexedNotNull { index, it ->
        val point = it.point
        val usedDistance = abs(point.y - slice)
        if (usedDistance > it.range) {
            null
        } else {
            val remainingDistance = it.range - usedDistance
            index to Point(point.x - remainingDistance, point.x + remainingDistance)
        }
    }

    val sorted = result.map { it.second }.sortedBy { it.x }

    val merged = mutableListOf<Point>()
    var toMerge = -1
    sorted.forEach { point ->
        if (toMerge == -1) {
            merged.add(point)
            toMerge = merged.lastIndex
        } else if (merged[toMerge].y >= point.x) {
            merged[toMerge] = Point(
                x = min(merged[toMerge].x, point.x),
                y = max(merged[toMerge].y, point.y)
            )
        } else {
            merged.add(point)
            toMerge = merged.lastIndex
        }
    }

    return merged.sumOf { it.y - it.x }
}

val day15p2: AoCTask = { input ->
    val (sensors, beacons) = readInput(input)

    val found = sensors.map { pointsJustOutOfRange(it, 4000000) }
        .flatten()
        .toSet()
        .filterNot { possiblePoint ->
            sensors.any { distance(possiblePoint, it.point) <= it.range }
        }
    val item = found.first()
    item.x * 4000000 + item.y
}

fun pointsJustOutOfRange(sensor: Sensor, limit: Int): Set<Point> {
    val loc = sensor.point
    val range = sensor.range + 1
    val from = loc.y - range
    val to = loc.y + range
    val points = mutableSetOf<Point>()
    for (i in from..to) {
        val usedDistance = abs(loc.y - i)
        val remainingDistance = range - usedDistance
        Point(loc.x - remainingDistance, i).let {
            if ((0..limit).contains(it.x) && (0..limit).contains(it.y)) {
                points.add(it)
            }
        }
        Point(loc.x + remainingDistance, i).let {
            if ((0..limit).contains(it.x) && (0..limit).contains(it.y)) {
                points.add(it)
            }
        }
    }
    return points
}


fun main() = runAoC(15, day15p1, day15p2)