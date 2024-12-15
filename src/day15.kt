import java.io.File

typealias World = Map<Pos, Char>

fun dir(c: Char): Dir = when (c) {
    '^' -> N
    'v' -> S
    '<' -> W
    '>' -> E
    else -> throw Exception("Invalid direction")
}

fun printMap(map: World, p: Pos = Pos(-1, -1)) {
    var row = 0
    while (Pos(row, 0) in map) {
        var col = 0
        while (Pos(row, col) in map) {
            print(if (Pos(row, col) == p) '@' else map[Pos(row, col)])
            col++
        }
        println()
        row++
    }
}

fun score(map: World, c: Char = 'O') = map.map { if (it.value == c) it.key.row * 100 + it.key.col else 0 }.sum()


/**
 * Returns a new world where map[p] == '.' or null of that's not possible.
 */
fun push(map: World, p: Pos, d: Dir): World? {
    if (map[p]!! !in "[]") return map
    if (d in listOf(W, E)) {
        val neededPos = p + d * 2
        if (map[neededPos] == '#') return null

        val newMap = push(map, neededPos, d)?.toMutableMap() ?: return null
        newMap[neededPos] = map[p + d]!!
        newMap[p + d] = map[p]!!
        newMap[p] = '.'
        return newMap
    } else {
        val neededPos1 = p + d
        val neededPos2 = if (map[p] == '[') p + d + E else p + d + W
        if (map[neededPos1] == '#' || map[neededPos2] == '#') return null

        val newMap =
            push(
                push(map, neededPos1, d) ?: return null,
                neededPos2, d
            )?.toMutableMap() ?: return null
        newMap[neededPos1] = map[p]!!
        newMap[neededPos2] = map[neededPos2 - d]!!
        newMap[p] = '.'
        newMap[neededPos2 - d] = '.'
        return newMap
    }
}

private operator fun Pos.times(i: Int) = Pos(row * i, col * i)

fun main(args: Array<String>) {
    val (mapPart, dirPart) = File(args[0]).readText().split("\n\n")
    val map = mapPart.lines().flatMapIndexed { row, line ->
        line.mapIndexed { col, char -> Pos(row, col) to char }
    }.toMap().toMutableMap()
    val directions = dirPart.lines().joinToString(separator = "").map(::dir)

    var pos = map.firstNotNullOf { if (it.value == '@') it.key else null }
    map[pos] = '.'

    directions.forEach { d ->
        when (map[pos + d]) {
            '.' -> pos += d
            '#' -> Unit
            'O' -> {
                var p = pos + d
                while (map[p] == 'O') p += d
                if (map[p] == '.') {
                    pos += d
                    map[pos] = '.'
                    map[p] = 'O'
                }
            }
        }
    }
    println("Part 1: ${score(map)}")

    var map2 = mapPart.lines().flatMapIndexed { row, line ->
        line.flatMapIndexed { col, char ->
            listOf(
                Pos(row, 2 * col) to when (char) {
                    'O' -> '['
                    else -> char
                }, Pos(row, 2 * col + 1) to when (char) {
                    'O' -> ']'
                    '@' -> '.'
                    else -> char
                }
            )
        }
    }.toMap().toMutableMap()
    pos = map2.firstNotNullOf { if (it.value == '@') it.key else null }
    map2[pos] = '.'
    directions.forEach { d ->
        when (map2[pos + d]) {
            '.' -> pos += d
            '#' -> Unit
            '[', ']' -> {
                val newMap = push(map2, pos + d, d)
                if (newMap != null) {
                    map2 = newMap.toMutableMap()
                    pos += d
                }
            }
        }
    }
    println("Part 2: ${score(map2, '[')}")
}
