import java.io.File


fun splitDigits(n: Long): Pair<Long, Long>? {
  val s = n.toString()
  val l = s.length
  if (l % 2 != 0) return null
  return Pair(s.take(l / 2).toLong(), s.substring(l / 2).toLong())
}

val cache = mutableMapOf<Pair<Long, Int>, Long>()
fun numStones(n: Long, blinks: Int): Long = cache.getOrPut(Pair(n, blinks)) {
  if (blinks == 0) return 1
  if (n == 0L) return numStones(1, blinks - 1)
  when (val parts = splitDigits(n)) {
    is Pair -> numStones(parts.first, blinks - 1) + numStones(parts.second, blinks - 1)
    else -> numStones(n * 2024L, blinks - 1)
  }
}

fun main(args: Array<String>) {
  val input = File(args[0]).readText().trim().split(" ").map(String::toLong)
  val part1 = input.sumOf { numStones(it, 25) }
  println("Part 1: $part1")
  val part2 = input.sumOf { numStones(it, 75) }
  println("Part 2: $part2")
}