val Char.asPriority
    get() = when {
        this.isLowerCase() -> this.code - 96
        else -> this.code - 38
    }

val day3p1: AoCTask = { input: String ->
    input.lines().filter { it.isNotBlank() }.fold(0) { acc, item ->
        val arr = item.toCharArray()
        val p1 = arr.take(item.length / 2).toSet()
        val p2 = arr.takeLast(item.length / 2).toSet()
        acc + p1.intersect(p2).first().asPriority
    }
}

val day3p2: AoCTask = { input ->
    input.lines()
        .filter { it.isNotEmpty() }
        .chunked(3)
        .fold(0) { totalPriorities, item ->
            item.map { it.toCharArray().toSet() }
                .reduce { acc, it -> acc.intersect(it) }
                .single().asPriority + totalPriorities
        }
}

fun main() = runAoC(3, day3p1, day3p2)
