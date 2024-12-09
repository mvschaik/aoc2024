import java.io.File


data class FileBlock(val fileNo: Int, val length: Int, var offset: Int = 0)
data class Empty(var length: Int, var offset: Int = 0)

class Part1(input: String) {
  private val files =
    input.filterIndexed { i, _ -> i % 2 == 0 }.map(Char::digitToInt).mapIndexed(::FileBlock)
  private val empty = input.filterIndexed { i, _ -> i % 2 != 0 }.map(Char::digitToInt)
  private var frontPtr = 0
  private var backPtr = files.lastIndex
  private var partLeft = files[backPtr].length

  private fun getBlocksFromBack(num: Int) = sequence {
    var left = num
    while (left > 0) {
      if (left >= partLeft) {
        // Yield rest of file.
        yield(FileBlock(files[backPtr].fileNo, partLeft))
        left -= partLeft
        partLeft = files[--backPtr].length
      } else {
        // Yield needed part
        yield(FileBlock(files[backPtr].fileNo, left))
        partLeft -= left
        left = 0
      }
    }
  }

  private fun getBlocksFromFront() = sequence {
    while (true) {
      if (frontPtr == backPtr) {
        yield(FileBlock(files[frontPtr].fileNo, partLeft))
        break
      }
      yield(files[frontPtr])
      yieldAll(getBlocksFromBack(empty[frontPtr++]))
    }
  }

  private fun getBlocks() = sequence {
    getBlocksFromFront().forEach { block ->
      repeat(block.length) { yield(block.fileNo) }
    }
  }

  fun solve() = getBlocks().mapIndexed { i, fileNo -> i.toULong() * fileNo.toULong() }.sum()
}

class Part2(input: String) {
  private var offset = 0
  private var fileList = mutableListOf<FileBlock>()
  private var emptyList = mutableListOf<Empty>()

  init {
    input.windowed(size = 2, step = 2, partialWindows = true).forEachIndexed { i, s ->
      val fileLen = s[0].digitToInt()
      fileList.add(FileBlock(i, fileLen, offset))
      offset += fileLen

      val emptyLen = s.getOrNull(1)?.digitToInt() ?: 0
      emptyList.add(Empty(emptyLen, offset))
      offset += emptyLen
    }
  }

  fun solve(): ULong {
    fileList.asReversed().forEach { file ->
      emptyList.firstOrNull { it.offset < file.offset && it.length >= file.length }.let { space ->
        if (space == null) return@let
        file.offset = space.offset
        space.offset += file.length
        space.length -= file.length
        emptyList.sortBy { it.offset }
      }
    }
    return fileList.sumOf { file ->
      (0..<file.length).sumOf { (it + file.offset).toULong() * file.fileNo.toULong() }
    }
  }
}


fun main(args: Array<String>) {
  val input = File(args[0]).readText().trim()
  println("Part 1: ${Part1(input).solve()}")
  println("Part 2: ${Part2(input).solve()}")
}