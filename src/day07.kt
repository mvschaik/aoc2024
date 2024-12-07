import java.io.File

infix fun Long.concat(other: Long): Long = (this.toString() + other.toString()).toLong()
operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = (this.first + other.first) to this.second + other.second
fun Iterable<Pair<Long, Long>>.sum(): Pair<Long, Long> = reduce { p1, p2 -> p1 + p2 }

fun main(args: Array<String>) {
    val (part1, part2) = File(args[0]).readLines().map { line ->
        val (sumPart, elementsPart) = line.split(": ")
        val sum = sumPart.toLong()
        val elements = elementsPart.split(" ").map { it.toLong() }

        var part1Values = listOf(elements[0])
        var part2Values = listOf(elements[0])
        for (e in elements.drop(1)) {
            val newPart1Values = mutableListOf<Long>()
            val newPart2Values = mutableListOf<Long>()
            for (v in part1Values) {
                newPart1Values += v + e
                newPart1Values += v * e
            }
            for (v in part2Values) {
                newPart2Values += v + e
                newPart2Values += v * e
                newPart2Values += v concat e
            }
            part1Values = newPart1Values
            part2Values = newPart2Values
        }
        Pair(
            if (sum in part1Values) sum else 0,
            if (sum in part2Values) sum else 0
        )
    }.sum()

    println("Part 1: $part1")
    println("Part 2: $part2")
}
