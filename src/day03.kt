import java.io.File

fun main(args: Array<String>) {
    val r = """mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""".toRegex()
    val input = File(args[0]).readText()
    var enabled = true
    var part1 = 0
    var part2 = 0
    r.findAll(input).forEach { group ->
        when(group.value){
            "do()" -> enabled = true
            "don't()" -> enabled = false
            else -> {
                val (x, y) = group.destructured
                val sum = x.toInt() * y.toInt()
                part1 += sum
                if (enabled) {
                    part2 += sum
                }
            }
        }
    }

    println("Part 1: $part1")
    println("Part 2: $part2")
}
