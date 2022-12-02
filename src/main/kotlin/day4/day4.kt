val day4p1: AoCTask = { input: String ->
    input.lines().filter { it.isNotBlank() }.fold(0) { acc, item ->
        val sections = item.split(",")
        val first = sections[0].split("-").map { it.toInt() }
        val second = sections[1].split("-").map { it.toInt() }

        when {
            first[0] >= second[0] && first[1] <= second[1] -> acc + 1
            first[0] <= second[0] && first[1] >= second[1] -> acc + 1
            else -> acc
        }
    }
}

val day4p2: AoCTask = { input ->
    input.lines().filter { it.isNotBlank() }.fold(0) { acc, item ->
        val sections = item.split(",")
        val first = sections[0].split("-").map { it.toInt() }
        val second = sections[1].split("-").map { it.toInt() }

        when {
            first[0] <= second[0] && first[1] >= second[0] -> acc + 1
            first[0] >= second[0] && second[1] >= first[0] -> acc + 1
            first[1] >= second[1] && first[0] <= second[1] -> acc + 1
            first[1] <= second[1] && first[1] >= second[0] -> acc + 1
            else -> acc
        }

    }
}

fun main() = runAoC(4, day4p1, day4p2)
