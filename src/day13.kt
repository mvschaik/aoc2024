import java.io.File

data class Coord(val x: Long, val y: Long)
data class Machine(val buttonA: Coord, val buttonB: Coord, val prize: Coord)

fun parseBtnLine(s: String): Coord {
    val (x, y) = """Button .: X\+(\d+), Y\+(\d+)""".toRegex().matchEntire(s)!!.destructured
    return Coord(x.toLong(), y.toLong())
}

fun parsePrize(s: String): Coord {
    val (x, y) = """Prize: X=(\d+), Y=(\d+)""".toRegex().matchEntire(s)!!.destructured
    return Coord(x.toLong(), y.toLong())
}

fun Machine.solve(): Pair<Long, Long>? {
    val b = (buttonA.x * prize.y - buttonA.y * prize.x) /
            (buttonA.x * buttonB.y - buttonA.y * buttonB.x)
    val a = (prize.x - b * buttonB.x) / buttonA.x
    return if (a * buttonA.x + b * buttonB.x == prize.x &&
        a * buttonA.y + b * buttonB.y == prize.y
    ) Pair(a, b) else null
}

fun main(args: Array<String>) {
    val machines = File(args[0]).readText().split("\n\n").map { input ->
        val (aLine, bLine, pLine) = input.lines()
        Machine(parseBtnLine((aLine)), parseBtnLine(bLine), parsePrize(pLine))
    }

    val part1 = machines.sumOf { machine ->
        val (a, b) = machine.solve() ?: Pair(0L, 0L)
        if (a in 0..100 && b in 0..100) 3 * a + b else 0
    }
    println("Part 1: $part1")

    val part2 = machines.sumOf { machine ->
        val (a, b) = Machine(
            machine.buttonA, machine.buttonB,
            Coord(
                machine.prize.x + 10000000000000,
                machine.prize.y + 10000000000000
            )
        ).solve() ?: Pair(0L, 0L)
        if (a >= 0 && b >= 0) 3 * a + b else 0
    }
    println("Part 2: $part2")
}
