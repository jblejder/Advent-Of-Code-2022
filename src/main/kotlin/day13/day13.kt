package day13

import AoCTask
import day13.Data.Literal
import day13.Data.Packet
import runAoC

private val testInput = """
    [1,1,3,1,1]
    [1,1,5,1,1]

    [[1],[2,3,4]]
    [[1],4]

    [9]
    [[8,7,6]]

    [[4,4],4,4]
    [[4,4],4,4,4]

    [7,7,7,7]
    [7,7,7]

    []
    [3]

    [[[]]]
    [[]]

    [1,[2,[3,[4,[5,6,7]]]],8,9]
    [1,[2,[3,[4,[5,6,0]]]],8,9]
""".trimIndent()

sealed class Data {
    class Packet(val parent: Packet? = null, val content: MutableList<Data> = mutableListOf()) : Data() {
        operator fun get(i: Int) = content[i]

        val size
            get() = content.size

        override fun toString(): String {
            return "$content"
        }
    }

    class Literal(val value: Int) : Data() {
        fun asPacket() = Packet(content = mutableListOf(Literal(value)))

        override fun toString(): String {
            return "$value"
        }
    }


}

class PackageBuilder {

    var packet: Packet? = null
    var currentPacket: Packet? = null

    var position = mutableListOf<Int>()
    var number: String = ""

    fun readChar(value: Char): PackageBuilder {
        when {
            value == '[' -> newPacket()
            value == ']' -> {
                if (number.isNotEmpty()) {
                    addValue(number.toInt())
                    number = ""
                }
                finishPacket()
            }
            value.isDigit() -> number += value
            value == ',' -> {
                if (number.isNotEmpty()) {
                    addValue(number.toInt())
                    number = ""
                }
            }
            else -> Unit
        }
        return this
    }

    private fun addValue(value: Int) {
        currentPacket!!.content.add(Literal(value))
    }

    private fun newPacket() {
        if (packet == null) {
            packet = Packet(parent = currentPacket)
            currentPacket = packet
        } else {
            val newPacket = Packet(parent = currentPacket)
            currentPacket!!.content.add(newPacket)
            currentPacket = newPacket
        }
    }

    private fun finishPacket() {
        if (currentPacket!!.parent == null) return
        else currentPacket = currentPacket!!.parent
    }
}

fun Iterator<Char>.readNumber(first: Char) {
    var txt = first


}

val day13p1: AoCTask = { input: String ->
    val indexes = mutableSetOf<Int>()
    val answer = input.trim().lines().chunked(3).foldIndexed(0) { index, acc, next ->
        println(next[0])
        println(next[1])
        val left = next[0].fold(PackageBuilder()) { acc, next -> acc.readChar(next) }.packet!!
        val right = next[1].fold(PackageBuilder()) { acc, next -> acc.readChar(next) }.packet!!
        println(left)
        println(right)

        println()

        if (inOrder(left, right) == true) {
            indexes.add(index + 1)
            acc + index + 1
        } else acc
    }

    println(indexes)
    answer
}

fun inOrder(leftPacket: Packet, rightPacket: Packet): Boolean? {
    print("")
    if (leftPacket.size == 0 && rightPacket.size == 0) return null
    for (i in 0..leftPacket.size) {
        // if packets are uneven size
        if (i == leftPacket.size || i == rightPacket.size) {
            return if (leftPacket.size == rightPacket.size) null
            else leftPacket.size < rightPacket.size
        }

        val left = leftPacket[i]
        val right = rightPacket[i]
        when {
            left is Literal && right is Literal -> {
                if (left.value == right.value) continue
                else return left.value < right.value
            }
            left is Packet && right is Packet -> {
                return inOrder(left, right) ?: continue
            }
            left is Packet && right is Literal -> {
                return inOrder(left, right.asPacket()) ?: continue
            }
            left is Literal && right is Packet -> {
                return inOrder(left.asPacket(), right) ?: continue
            }
        }
    }
    return null
}

val dividers = """
    [[2]]
    [[6]]
""".trimIndent()


val day13p2: AoCTask = { input: String ->
    val input = input.trim().lines().filter { it.isNotEmpty() }.foldIndexed(listOf<Packet>()) { index, acc, next ->
        acc + next.fold(PackageBuilder()) { acc, next -> acc.readChar(next) }.packet!!
    }
    val dividers =
        dividers.trim().lines().filter { it.isNotEmpty() }.foldIndexed(listOf<Packet>()) { index, acc, next ->
            acc + next.fold(PackageBuilder()) { acc, next -> acc.readChar(next) }.packet!!
        }

    val ansewer = (input + dividers).sortedWith { o1, o2 ->
        when (inOrder(o1, o2)) {
            true -> -1
            false -> 1
            null -> 0
        }
    }

    ansewer.forEach { println(it) }

    val i1 = ansewer.indexOf(dividers[0]) + 1
    val i2 = ansewer.indexOf(dividers[1]) + 1

    i1 * i2
}

fun main() = runAoC(13, day13p1, day13p2)
