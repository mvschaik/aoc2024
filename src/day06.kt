import java.io.File


enum class Block { EMPTY, WALL }

typealias Dir = Pos

fun Dir.turnRight(): Pos = Pos(col, -row)

fun willLoop(world: Map<Pos, Block>, candidate: Pos, startingPos: Pos, startingDir: Dir): Boolean {
    val visited = mutableSetOf<Pair<Pos,Dir>>()
    var pos = startingPos
    var dir = startingDir
    while (world.contains(pos)) {
        visited.add(Pair(pos, dir))
        while (pos + dir == candidate || world.getOrDefault(pos + dir, Block.EMPTY) == Block.WALL) {
            dir = dir.turnRight()
        }
        pos += dir
        if (visited.contains(Pair(pos, dir))) return true
    }
    return false
}

fun main(args: Array<String>) {
    var startingPos = Pos(0, 0)
    val startingDir = Dir(-1, 0)
    val world = File(args[0]).readLines().flatMapIndexed { row, line ->
        line.mapIndexed { col, value ->
            if (value == '^') startingPos = Pos(row, col)
            Pos(row, col) to if (value == '#') Block.WALL else Block.EMPTY
        }
    }.toMap()

    var pos = startingPos
    var dir = startingDir
    val visited = mutableSetOf<Pos>()
    while (world.contains(pos)) {
        visited.add(pos)
        while (world.getOrDefault(pos + dir, Block.EMPTY) == Block.WALL) {
            dir = dir.turnRight()
        }
        pos += dir
    }
    println("Part 1: ${visited.size}")

    val part2 = visited.filter { willLoop(world, it, startingPos, startingDir) }
    println("Part 2: ${part2.size}")
}
