import java.io.File
import kotlin.math.abs


fun main(args: Array<String>) {
    val (firstList, secondList) = File(args[0]).readLines().map { line ->
        val parts = line.trim().split("""\s+""".toRegex(), limit = 2)
        Pair(parts[0].toInt(), parts[1].toInt())
    }.unzip()

    // Part 1
    val part1 = (firstList.sorted() zip secondList.sorted())
        .sumOf { (first, second) -> abs(first - second) }
    println("Part 1: $part1")

    // Part 2
    val countsInSecondList = secondList.groupBy { it }.mapValues { it.value.size }
    val part2 = firstList.map { it * countsInSecondList.getOrDefault(it, 0) }.sum()
    println("Part 2: $part2")
}
