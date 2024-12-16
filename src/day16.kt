import java.io.File
import java.util.PriorityQueue

data class Reindeer(val pos: Pos, val dir: Dir)
typealias Path = Set<Pos>

fun main(args: Array<String>) {
  val map = File(args[0]).readLines().flatMapIndexed { row, line ->
    line.mapIndexed { col, char -> Pos(row, col) to char }
  }.toMap()
  val start = Reindeer(map.firstNotNullOf { if (it.value == 'S') it.key else null }, E)

  val q = PriorityQueue<Triple<Reindeer, Path, Int>> { a, b -> a.third - b.third }
  q.offer(Triple(start, setOf(start.pos), 0))

  val shortestPathsTo = mutableMapOf<Reindeer, Pair<Int, Set<Pos>>>()
  while (q.isNotEmpty()){
    val (r, p, s) = q.poll()
    var paths = shortestPathsTo[r]
    if (paths != null) {
      if (paths.first != s) continue
      // Merge paths.
      paths = s to paths.second + p
    } else {
      paths = s to p
      shortestPathsTo[r] = paths
    }

    if (map[r.pos] == 'E') {
      println("Part 1: $s")
      println("Part 2: ${paths.second.size}")
      break
    }

    for (d in listOf(S, W, N, E)) {
      if (map[r.pos + d] == '#') continue
      if (d == r.dir) q.offer(Triple(r.copy(pos = r.pos + d), paths.second+(r.pos+d) , s + 1))
      else q.offer(Triple(r.copy(dir = d), paths.second, s + 1000))
    }
  }
}