import java.io.File


fun run(program: List<Int>, initialRegs: List<Long>): List<Int> {
    val regs = initialRegs.toLongArray()
    fun combo(i: Int): Long = when (i) {
        in 0..3 -> i.toLong()
        in 4..6 -> regs[i - 4]
        else -> throw Error("Invalid combo operand")
    }

    var ip = 0
    val out = mutableListOf<Int>()
    while (ip in program.indices) {
        val operand = program[ip + 1]
        when (program[ip]) {
            0 -> regs[0] = regs[0] shr combo(operand).toInt()
            1 -> regs[1] = regs[1] xor operand.toLong()
            2 -> regs[1] = combo(operand) and 7
            3 -> if (regs[0] != 0L) ip = operand - 2
            4 -> regs[1] = regs[1] xor regs[2]
            5 -> out += (combo(operand) and 7).toInt()
            6 -> regs[1] = regs[0] shr combo(operand).toInt()
            7 -> regs[2] = regs[0] shr combo(operand).toInt()
            else -> throw Error("Invalid instruction ${program[ip]} @ $ip")
        }
        ip += 2
    }
    return out
}

fun main(args: Array<String>) {
    val (regsPart, instrPart) = File(args[0]).readText().split("\n\n")
    val regs = regsPart.lines().map { it.split(": ")[1].toLong() }
    val program = instrPart.split(": ")[1].split(",").map { it.trim().toInt() }

    println("Part 1: ${run(program, regs).joinToString(",")}")

    data class PartialSolution(val a: Long, val mask: Long) {
        fun set(a: Long, mask: Long): PartialSolution? {
            if (this.mask and mask and this.a !=
                this.mask and mask and a) return null
            return PartialSolution(a or this.a, mask or this.mask)
        }
        fun setWithOffset(a: Int, offset: Int) = setWithOffset(a.toLong(), offset)
        fun setWithOffset(a: Long, offset: Int) = set(a shl offset, 7L shl offset)
    }

    // B = A and 7      ///  2,4
    // B = B xor 7      ///  1,7
    // C = A >> B       ///  7,5
    // A = A >> 3       ///  0,3
    // B = B xor C      ///  4,4
    // B = B xor 7      ///  1,7
    // out << B and 7   ///  5,5
    // if A > 0 goto 0  ///  3,0

    var solutions = listOf(PartialSolution(0, 0))
    for ((i, d) in program.withIndex()) {
        val offset = i * 3
        solutions = solutions.flatMap { s ->
            (0..7).mapNotNull { a0 ->
                // digit = A[0] xor A[!A[0]]
                s.setWithOffset(a0, offset)
                    ?.setWithOffset(a0 xor d, offset + (a0 xor 7))
            }
        }
    }
    println("Part 2: ${solutions.minBy { it.a }.a}")
}
