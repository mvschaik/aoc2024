import java.io.File

data class Pos(val row: Int, val col: Int) {
    operator fun plus(other: Pos) = Pos(row + other.row, col + other.col)
}

fun findWord(map: Map<Pos, Char>, start: Pos, word: String = "XMAS"): Int =
    (-1..1).flatMap { row ->
        (-1..1).map { col -> Pos(row, col) }
    }
        .filter { it != Pos(0, 0) }
        .sumOf { findWord(map, start, word, it) }

fun findWord(map: Map<Pos, Char>, start: Pos, word: String = "XMAS", dir: Pos): Int {
    if (word.isEmpty()) return 1
    if (!map.containsKey(start)) return 0
    if (map[start] != word[0]) return 0
    return findWord(map, start + dir, word.substring(1), dir)
}

val SE = Pos(1, 1)
val NW = Pos(-1, -1)
val SW = Pos(1, -1)
val NE = Pos(-1, 1)

fun findXMas(map: Map<Pos, Char>, pos: Pos): Boolean {
    if (map[pos] != 'A') return false
    val otherLetters = setOf('M', 'S')
    return setOf(map[pos + SE], map[pos + NW]) == otherLetters &&
            setOf(map[pos + NE], map[pos + SW]) == otherLetters
}

fun main(args: Array<String>) {
    val map = File(args[0]).readLines().flatMapIndexed { row, s ->
        s.mapIndexed { col, c -> Pos(row, col) to c }
    }.toMap()

    val part1 = map.keys.sumOf { findWord(map, it) }
    println("Part 1: $part1")

    val part2 =
        map.keys.filter { it.col != 0 && it.row != 0 && map.containsKey(it + SE) }
            .count { findXMas(map, it) }
    println("Part 2: $part2")
}
