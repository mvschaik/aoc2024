import java.io.File

enum class State { OPEN, DAMAGED }

class UnionFind<T> {
  private val conn = mutableMapOf<T, T>()
  private val size = mutableMapOf<T, Int>()

  fun add(x: T) {
    conn[x] = x
    size[x] = 1
  }

  fun find(x: T): T {
    var toFind = x
    while (conn[toFind] != toFind) {
      conn[toFind] = conn[conn[toFind]!!]!!
      toFind = conn[toFind]!!
    }
    return toFind
  }

  fun union(a: T, b: T) {
    val ar = find(a)
    val br = find(b)
    if (ar == br) return
    if (size[ar]!! < size[br]!!) {
      conn[ar] = br; size[br] = size[br]!! + size[ar]!!
    } else {
      conn[br] = ar; size[ar] = size[ar]!! + size[br]!!
    }
  }
}

fun neighbors(p: Vec) = listOf(p + Vec(0, 1), p + Vec(0, -1), p + Vec(1, 0), p + Vec(-1, 0))
fun openNeighbors(world: Map<Vec, State>, p: Vec) = neighbors(p).filter { world[it] == State.OPEN }

fun buildWorld(bytes: Set<Vec>, size: Long) =
  (0L..size).flatMap { x ->
    (0L..size).map { y ->
      val p = Vec(x, y)
      p to if (p in bytes) State.DAMAGED else State.OPEN
    }
  }.toMap()


fun main(args: Array<String>) {
  val bytes = File(args[0]).readLines().map {
    val (x, y) = it.split(",").map(String::toLong)
    Vec(x, y)
  }
  val test = args[0].endsWith("test.txt")
  val size = if (test) 6L else 70L

  val part1Bytes = if (test) bytes.take(12) else bytes.take(1024)
  val part1World = buildWorld(part1Bytes.toSet(), size)

  val queue = ArrayDeque<Pair<Vec, Set<Vec>>>()
  queue.addLast(Vec(0, 0) to setOf(Vec(0, 0)))
  val seen = mutableSetOf<Vec>()
  while (queue.isNotEmpty()) {
    val (pos, path) = queue.removeFirst()

    if (pos in seen) continue
    seen += pos

    if (pos == Vec(size, size)) {
      println("Part 1: ${path.size - 1}")
      break
    }
    openNeighbors(part1World, pos).forEach { queue.addLast(it to path + it )}
  }

  val uf = UnionFind<Vec>()
  val part2World = buildWorld(bytes.toSet(), size).toMutableMap()
  part2World.keys.forEach(uf::add)
  part2World.filter { it.value == State.OPEN }.keys.forEach { p ->
    openNeighbors(part2World, p).forEach { uf.union(p, it) }
  }

  for (p in bytes.reversed()) {
    part2World[p] = State.OPEN
    openNeighbors(part2World, p).forEach { uf.union(p, it) }

    if (uf.find(Vec(0, 0)) == uf.find(Vec(size, size))) {
      println("Part 2: ${p.x},${p.y}")
      break
    }
  }
}