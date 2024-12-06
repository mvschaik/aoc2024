import java.io.File
import java.util.Comparator

fun <T> middle(list: List<T>): T = list[list.size/2]

fun main(args: Array<String>) {
  val (orderingsPart, updatesPart) = File(args[0]).readText().split("\n\n")
  val graph = orderingsPart.lines().map { line ->
    val (before, after) = line.split("|")
    before.toInt() to after.toInt()
  }.groupBy { it.first }.mapValues { it.value.map { it.second }.toMutableSet() }
  val updates = updatesPart.lines().map { it.split(",").map(String::toInt) }

  fun isOrdered(update: List<Int>): Boolean {
    val seen: MutableSet<Int> = mutableSetOf()
    for (i in update) {
      if ((graph.getOrDefault(i, emptySet()) intersect seen).isNotEmpty()) {
        return false
      }
      seen.add(i)
    }
    return true
  }
  val part1 = updates.filter(::isOrdered).map(::middle).sum()

  val comparator = Comparator { u1: Int, u2: Int ->
    if (isOrdered(listOf(u1, u2))) 1 else -1
  }
  val part2 = updates.filterNot(::isOrdered).map { update ->
    update.sortedWith(comparator)
  }.map(::middle).sum()

  println("Part 1: $part1")
  println("Part 2: $part2")
}