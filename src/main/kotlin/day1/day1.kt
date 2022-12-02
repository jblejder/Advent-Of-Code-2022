val day1p1: AoCTask = { input: String ->
    val result = input.lines().fold(Pair(0, 0)) { acc, c ->
        var (current, max) = acc
        when {
            c.isBlank() -> {
                if (current > max) {
                    max = current
                }
                current = 0
            }
            else -> {
                current += c.toInt()
            }

        }
        Pair(current, max)
    }
    result.second.toString()
}


val day1p2: AoCTask = { input ->
    input.lines().fold(listOf(0)) { acc, line ->
        val mAcc = acc.toMutableList()
        when {
            line.isEmpty() -> listOf(0) + acc
            else -> {
                mAcc[0] = mAcc[0] + line.toInt()
                mAcc
            }
        }
    }.sortedDescending()
        .take(3)
        .sum()
}

fun main() = runAoC(1, day1p1, day1p2)