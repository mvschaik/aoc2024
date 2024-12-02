import java.io.File
import kotlin.math.abs
import kotlin.math.sign


fun valid(levels: List<Int>): Boolean {
  val sign = (levels[1] - levels[0]).sign
  return levels.windowed(2).all { pair ->
    val diff = pair[1] - pair[0]
    diff.sign == sign && abs(diff) in 1..3
  }
}

fun main(args: Array<String>) {
  val (part1, part2) = File(args[0]).readLines().map { line ->
    val levels = line.trim().split("""\s+""".toRegex()).map(String::toInt)

    val part1Valid = valid(levels)

    val part2Valid = levels.indices
      .map { index -> levels.filterIndexed { i, _ -> i != index } }
      .any { valid(it) }

    Pair(part1Valid, part2Valid)
  }.fold(Pair(0,0)) { result, validLevels ->
    Pair(result.first + if (validLevels.first) 1 else 0,
         result.second + if (validLevels.second) 1 else 0)
  }

  println("Part 1: $part1")
  println("Part 2: $part2")
}