import java.io.File


val N = Pos(-1, 0)
val S = Pos(1, 0)
val W = Pos(0, -1)
val E = Pos(0, 1)

fun main(args: Array<String>) {
  val trailHeads = mutableSetOf<Pos>()
  val world = File(args[0]).readLines().flatMapIndexed { row, line ->
    line.mapIndexed { col, c ->
      if (c == '0') trailHeads.add(Pos(row, col))
      Pos(row, col) to c.digitToInt() }
  }.toMap()

  fun neighbors(p: Pos) =
    listOf(N, S, W, E).map { p + it }.filter { it in world && world[it] == world[p]!! + 1 }

  fun walkTrails(p: Pos, countTracks: Boolean): Int {
    val q = mutableListOf(p)
    val visited = mutableSetOf<Pos>()
    var score = 0
    while (q.isNotEmpty()) {
      val curr = q.removeLast()
      if (countTracks && curr in visited) continue
      visited.add(curr)

      if (world[curr] == 9) score++
      else q.addAll(neighbors(curr))
    }
    return score
  }

  val part1 = trailHeads.sumOf { walkTrails(it, true) }
  val part2 = trailHeads.sumOf { walkTrails(it, false) }
  println("Part 1: $part1")
  println("Part 2: $part2")
}