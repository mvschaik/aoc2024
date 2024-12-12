import java.io.File

fun readMapFromFile(fileName: String): Pair<Map<Pos, Char>, Size> {
  var maxRow = 0
  var maxCol = 0
  return Pair(File(fileName).readLines().flatMapIndexed { row, line ->
    line.mapIndexed { col, c ->
      maxRow = maxOf(maxRow, row)
      maxCol = maxOf(maxCol, col)
      Pos(row, col) to c
    }
  }.toMap(), Pos(maxRow, maxCol))
}

fun Iterable<Pair<Int, Int>>.pairwiseSum() = reduce { (a1, a2), (b1, b2) -> Pair(a1 + b1, a2 + b2) }

fun main(args: Array<String>) {
  val (world, _) = readMapFromFile(args[0])

  fun same(p1: Pos, p2: Pos) = world.getOrDefault(p1, '-') == world.getOrDefault(p2, '-')

  val visited = mutableSetOf<Pos>()
  val (part1, part2) = world.keys.map { pos ->
    val queue = mutableListOf(pos)
    var surface = 0
    var bounds = 0
    var corners = 0
    while (queue.isNotEmpty()) {
      val p = queue.removeLast()
      if (p in visited) continue
      visited += p

      surface++
      listOf(N, W, S, E).forEach {
        val neighbor = p + it
        if (same(neighbor, p)) {
          queue += neighbor
        } else {
          bounds++
        }
      }
      corners += listOf(NW, SW, SE, NE).count {
        val a = Pos(it.row, 0)
        val b = Pos(0, it.col)
        // For SW:
        // AA 0  AA 1  AX 0  AA 0  AX 1  AA 0  AX 0  AX 1
        // AA    AX    AX    XA    XX    XX    AA    XA
        (!same(p + it, p) && same(p + a, p) && same(p + b, p)) ||
          (!same(p + a, p) && !same(p + b, p))
      }
    }

    Pair(bounds * surface, corners * surface)
  }.pairwiseSum()
  println("Part 1: $part1")
  println("Part 2: $part2")
}