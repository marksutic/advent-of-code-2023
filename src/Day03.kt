fun main() {

  fun part1(input: List<String>): Int {
    val coordinates: List<List<Char>> = input.map { it.toCharArray().toList() }
    val partNumbers = coordinates.toParts()
    return partNumbers.filter { coordinates.hasAdjacentSymbol(it) }
      .sumOf { it.value }
  }

  fun part2(input: List<String>): Int {
    val coordinates: List<List<Char>> = input.map { it.toCharArray().toList() }

    return coordinates.findAllNumbersWithGears()
      .groupBy { it.gear }
      .mapNotNull { partNumber ->
        if (partNumber.value.size == 2) {
          partNumber.value[0].value * partNumber.value[1].value
        } else {
          null
        }
      }
      .sum()
  }

  val testInputFirstStar = readInput("Day03_1_test")
  check(part1(testInputFirstStar) == 4361)

  val input = readInput("Day03")
  println("first star: ${part1(input)}")

  val testInputSecondStar = readInput("Day03_2_test")
  check(part2(testInputSecondStar) == 467835)

  println("second star: ${part2(input)}")
}


private data class PartNumberWithLocation(
  val value: Int,
  val rowIndex: Int,
  val columnStart: Int,
  val columnEnd: Int,
)

private fun List<List<Char>>.toParts(): List<PartNumberWithLocation> {
  val partNumbers: MutableList<PartNumberWithLocation> = mutableListOf()

  for (rowIndex in this.indices) {
    var partNumber = 0
    var numberStartPosition = -1
    var numberEndPosition = -1
    for (columnIndex in this[rowIndex].indices + 1) {
      val char = this[rowIndex][columnIndex]
      "char: $char".printlnDebug()
      if (char.isDigit()) {
        if (numberStartPosition == -1) {
          numberStartPosition = columnIndex
          numberEndPosition = columnIndex
        } else {
          numberEndPosition = columnIndex
        }
        "numberStartPosition: $numberStartPosition".printlnDebug()
        "numberEndPosition: $numberEndPosition".printlnDebug()
        partNumber = 10 * partNumber + char.digitToInt()
        "partNumber: $partNumber".printlnDebug()
      } else {
        if (partNumber > 0) {
          partNumbers += PartNumberWithLocation(partNumber, rowIndex, numberStartPosition, numberEndPosition)
        }
        numberStartPosition = -1
        numberEndPosition = -1
        partNumber = 0
      }
    }
  }

  return partNumbers
}

private fun List<List<Char>>.hasAdjacentSymbol(partNumber: PartNumberWithLocation): Boolean {
  partNumber.printlnDebug()
  val top = (partNumber.rowIndex - 1).coerceAtLeast(0)
  val down = (partNumber.rowIndex + 1).coerceAtMost(this.lastIndex)
  val left = (partNumber.columnStart - 1).coerceAtLeast(0)
  val right = (partNumber.columnEnd + 1).coerceAtMost(this[partNumber.rowIndex].lastIndex)
  "top: $top".printlnDebug()
  "down: $down".printlnDebug()
  "left: $left".printlnDebug()
  "right: $right".printlnDebug()
  var hasSymbol = false
  (top..down).forEach { row ->
    (left..right).forEach { column ->
      val charValue = this[row][column]
      "charValue: $charValue".printlnDebug()
      if (charValue != '.' && !charValue.isDigit()) {
        hasSymbol = true
      }
    }
  }
  return hasSymbol
}

private data class PartNumberWithGearLocation(val value: Int,val gear: Gear)

private data class Gear(val row:Int, val column: Int)

private fun List<List<Char>>.findAllNumbersWithGears(): List<PartNumberWithGearLocation> {
  val parts = mutableListOf<PartNumberWithGearLocation>()
  for (rowIndex in this.indices) {
    var partNumber = 0
    var numberStartPosition = -1
    var numberEndPosition = -1
    for (columnIndex in this[rowIndex].indices + 1) {
      when (val char = this[rowIndex][columnIndex]) {
        in '0'..'9' -> {
          if (numberStartPosition == -1) {
            numberStartPosition = columnIndex
            numberEndPosition = columnIndex
          } else {
            numberEndPosition = columnIndex
          }
          partNumber = 10 * partNumber + char.digitToInt()
        }
        else -> {
          if (numberStartPosition != -1) {
            "number: $partNumber".printlnDebug()
            parts += this.getGears(rowIndex, numberStartPosition, numberEndPosition).map { PartNumberWithGearLocation(partNumber, Gear(it.first, it.second)) }
            numberStartPosition = -1
            numberEndPosition = -1
            partNumber = 0
          }
        }
      }
    }
    if (numberStartPosition != -1) {
      parts += this.getGears(rowIndex, numberStartPosition, numberEndPosition).map { PartNumberWithGearLocation(partNumber, Gear(it.first, it.second)) }
      partNumber = 0
    }
  }
  return parts
}

private fun List<List<Char>>.getGears(row: Int, startColumn: Int, endColumn: Int): List<Pair<Int, Int>> {
  val top = (row - 1).coerceAtLeast(0)
  val down = (row + 1).coerceAtMost(this.lastIndex)
  val left = (startColumn - 1).coerceAtLeast(0)
  val right = (endColumn + 1).coerceAtMost(this[0].lastIndex)
  return (top..down).flatMap { rowIndex ->
    (left..right).mapNotNull { columnIndex ->
      if (this[rowIndex][columnIndex] == '*') {
        Pair(rowIndex, columnIndex)
      } else {
        null
      }
    }
  }
}

