import kotlin.math.abs

fun findPath(world: Map<Pos, Char>): List<Pos> {
    val start = world.firstNotNullOf { if (it.value == 'S') it.key else null }
    val q = mutableListOf(listOf(start))
    while (q.isNotEmpty()) {
        val path = q.removeLast()
        val p = path.last()

        if (world[p] == 'E') {
            return path
        }

        for (d in listOf(N, S, W, E)) {
            val next = p + d
            if (next !in path && world[next] != '#') {
                q.add(path + next)
            }
        }
    }
    return emptyList()
}

fun dist(a: Pos, b: Pos): Int = abs(a.row - b.row) + abs(a.col - b.col)

fun main(args: Array<String>) {
    val (world, _) = readMapFromFile(args[0])

    val test = args[0].endsWith("test.txt")
    val min = if (test) 50 else 100

    val path = findPath(world)
    val times = path.mapIndexed { index, p -> p to index }.toMap()
    val skips = path.flatMap { p ->
        listOf(N + N, N + E, E + E, E + S, S + S, S + W, W + W, W + N)
            .filter { p + it in times && times[p + it]!! < times[p]!! - 2 }
            .map { times[p]!! - times[p + it]!! - 2 }
    }.groupBy { it }.mapValues { it.value.size }
    if (test) println("Part 1 test: ${skips.values.sum()}")
    println("Part 1: ${skips.filterKeys { it >= min }.values.sum()}")

    val part2 = mutableMapOf<Int, Int>()
    for ((i1, p1) in path.withIndex()) {
        if (2 + i1 + min >= path.size) break
        for ((ii2, p2) in path.subList(2 + i1 + min, path.size).withIndex()) {
            val i2 = 2 + i1 + ii2 + min
            val d = dist(p1, p2)
            if (d > 20) continue
            val win = i2 - i1 - d
            if (win < min) continue
            part2[win] = part2.getOrDefault(win, 0) + 1
        }
    }
    if (test) println("Part 2 test: $part2")
    println("Part 2: ${part2.values.sum()}")
}
