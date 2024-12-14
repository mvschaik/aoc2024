import java.io.File


data class Robot(val pos: Vec, val vel: Vec)

fun Iterable<Int>.product() = reduce { a, b -> a * b }

fun robotsToString(robots: Iterable<Robot>, size: Vec): String {
    val lookup: Map<Pair<Boolean, Boolean>, Char> = mapOf(
        Pair(false, false) to ' ',
        Pair(true, false) to '▀',
        Pair(false, true) to '▄',
        Pair(true, true) to '█'
    )
    val positions = robots.map { it.pos }.toSet()
    return (0..size.y step 2).joinToString(separator = "\n") { y ->
        (0..size.x).map { x -> lookup[Pair(Vec(x, y) in positions, Vec(x, y + 1) in positions)] }
            .joinToString(separator = "")
    }
}


fun main(args: Array<String>) {
    val size = if (args[0].endsWith("test.txt")) Vec(11, 7) else Vec(101, 103)
    val robots = File(args[0]).readLines().map { line ->
        val (px, py, vx, vy) = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex().matchEntire(line)!!.destructured
        Robot(Vec(px.toLong(), py.toLong()), Vec(vx.toLong(), vy.toLong()))
    }

    fun quadrant(p: Vec) = when {
        p.x in 0..<(size.x / 2) && p.y in 0..<(size.y / 2) -> 0
        p.x > size.x / 2 && p.y in 0..<(size.y / 2) -> 1
        p.x in 0..<(size.x / 2) && p.y > size.y / 2 -> 2
        p.x > size.x / 2 && p.y > size.y / 2 -> 3
        else -> null
    }

    val part1 = robots.asSequence().map { robot ->
        robot.copy(pos = (robot.pos + robot.vel * 100).mod(size))
    }.map { quadrant(it.pos) }.filterNotNull().groupBy { it }.map { it.value.size }.toList().product()
    println("Part 1: $part1")

    var r = robots
    for (i in 0..size.x * size.y) {
        val s = robotsToString(r, size)
        if ("█████████" in s) {
            println("Part 2: $i")
            println(s)
            break
        }
        r = r.map { it.copy(pos = (it.pos + it.vel).mod(size)) }
    }
}
