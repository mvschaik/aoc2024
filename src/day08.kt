import java.io.File

typealias Size = Pos

fun <T> List<T>.pairs() =
    this.flatMap { first ->
        this.flatMap { second ->
            if (first != second) listOf(
                Pair(
                    first,
                    second
                )
            ) else emptyList()
        }
    }

operator fun Pos.minus(other: Pos) = Pos(this.row - other.row, this.col - other.col)

fun Pair<Pos, Pos>.antinode() = this.second + this.second - this.first

fun Size.contains(p: Pos) = p.row in 0..this.row && p.col in 0..this.col

fun Pair<Pos, Pos>.antinodes(bounds: Size): List<Pos> {
    var a = this.first
    var b = this.second
    return sequence {
        while (bounds.contains(b)) {
            yield(b)
            b = Pair(a, b).antinode().also { a = b }
        }
    }.toList()
}


fun main(args: Array<String>) {
    var maxRow = 0
    var maxCol = 0
    val antennas = File(args[0]).readLines().flatMapIndexed { row, line ->
        line.flatMapIndexed { col, value ->
            maxRow = maxOf(maxRow, row)
            maxCol = maxOf(maxCol, col)
            if (value != '.') listOf(value to Pos(row, col)) else emptyList()
        }
    }.groupBy({ it.first }, { it.second })
    val bounds = Size(maxRow, maxCol)

    val part1 = antennas.values.flatMap { positions ->
        positions.pairs().map { it.antinode() }.filter { bounds.contains(it) }
    }.toSet().count()
    println("Part1: $part1")

    val part2 = antennas.values.flatMap { positions ->
        positions.pairs().flatMap { it.antinodes(bounds) }
    }.toSet().count()
    println("Part2: $part2")
}
