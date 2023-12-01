fun main() {

  fun part1(input: List<String>): Int = input.map { str ->
    str[str.indexOfFirst { it.isDigit() }].toString() + str[str.indexOfLast { it.isDigit() }].toString()
  }.sumOf { it.toInt() }

  fun part2(input: List<String>): Int {
    val validDigitStrings = mapOf(
      "one" to 1,
      "two" to 2,
      "three" to 3,
      "four" to 4,
      "five" to 5,
      "six" to 6,
      "seven" to 7,
      "eight" to 8,
      "nine" to 9,
      "1" to 1,
      "2" to 2,
      "3" to 3,
      "4" to 4,
      "5" to 5,
      "6" to 6,
      "7" to 7,
      "8" to 8,
      "9" to 9,
    )
    return input.map { str ->
      val firstIndexes = validDigitStrings.mapValues { str.indexOf(it.key) }.filterValues { it >= 0 }
      val lastIndexes = validDigitStrings.mapValues { str.lastIndexOf(it.key) }.filterValues { it >= 0 }
      "${validDigitStrings[firstIndexes.minBy { it.value }.key]}${validDigitStrings[lastIndexes.maxBy { it.value }.key]}"
    }.sumOf { it.toInt() }
  }

  val testInputFirstStar = readInput("Day01_1_test")
  check(part1(testInputFirstStar) == 142)

  val input = readInput("Day01")
  println("first star: ${part1(input)}")

  val testInputSecondStar = readInput("Day01_2_test")
  check(part2(testInputSecondStar) == 281)

  println("second star: ${part2(input)}")
}

