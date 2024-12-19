import java.io.File

class Matcher(private val patterns: List<String>) {
  private val cache = mutableMapOf<String, Long>()
  fun matches(s: String): Long = cache.getOrPut(s) {
    if (s.isEmpty()) return@getOrPut 1
    patterns.filter { s.startsWith(it) }
      .sumOf { matches(s.substring(it.length)) }
  }
}

fun main(args: Array<String>) {
  val (towelPart, designPart) = File(args[0]).readText().split("\n\n")
  val towels = towelPart.split(", ")
  val towelRegex = "^(${towels.joinToString("|")})+$".toRegex()
  val part1 = designPart.lines().count { towelRegex.matches(it) }
  println("Part 1: $part1")

  val m = Matcher(towels)
  val part2 = designPart.trim().lines().sumOf { m.matches(it) }
  println("Part 2: $part2")
}