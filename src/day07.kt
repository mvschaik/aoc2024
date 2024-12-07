import java.io.File

infix fun Long.concat(other: Long): Long = (this.toString() + other.toString()).toLong()
operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = (this.first + other.first) to this.second + other.second
fun Iterable<Pair<Long, Long>>.pairwiseSum(): Pair<Long, Long> = reduce { p1, p2 -> p1 + p2 }

fun main(args: Array<String>) {
    val (part1, part2) = File(args[0]).readLines().map { line ->
        val (sumPart, elementsPart) = line.split(": ")
        val result = sumPart.toLong()
        val elements = elementsPart.split(" ").map { it.toLong() }

        val p1results = elements.drop(1).fold(listOf(elements[0])) { acc, e ->
            acc.flatMap { listOf(it + e, it * e) }
        }
        val p2results = elements.drop(1).fold(listOf(elements[0])) { acc, e ->
            acc.flatMap { listOf(it + e, it * e, it concat e) }
        }

        Pair(
            if (result in p1results) result else 0,
            if (result in p2results) result else 0
        )
    }.pairwiseSum()

    println("Part 1: $part1")
    println("Part 2: $part2")
}
