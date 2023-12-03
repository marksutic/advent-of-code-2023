fun main() {

  fun part1(input: List<String>): Int {
    val gameRecords = input.map { it.toGameRecord() }

    val redCubes = 12
    val greenCubes = 13
    val blueCubes = 14

    return gameRecords.filter { it.sets.isPossible(redCubes, greenCubes, blueCubes) }
      .sumOf { it.id }
  }

  fun part2(input: List<String>): Int {
    val gameRecords = input.map { it.toGameRecord() }

    return gameRecords.sumOf { gameRecord ->
      val maxRed = gameRecord.sets.maxOf { it.red }
      val maxGreen = gameRecord.sets.maxOf { it.green }
      val maxBlue = gameRecord.sets.maxOf { it.blue }
      maxRed * maxGreen * maxBlue
    }
  }

  val testInputFirstStar = readInput("Day02_1_test")
  check(part1(testInputFirstStar) == 8)

  val input = readInput("Day02")
  println("first star: ${part1(input)}")

  val testInputSecondStar = readInput("Day02_1_test")
  check(part2(testInputSecondStar) == 2286)

  println("second star: ${part2(input)}")
}

private data class GameSet(val red: Int, val green: Int, val blue: Int)
private data class GameRecord(val id: Int, val sets: List<GameSet>)

private fun String.toGameRecord(): GameRecord {
  val id = this.substringAfter("Game ").substringBefore(":").toInt()
  val setsStrings = this.substringAfter(": ").split(";")
  return GameRecord(id = id, sets = setsStrings.map { it.toGameSet() })
}

private fun String.toGameSet(): GameSet {
  val colorRegex = "(\\d+)\\s*(red|green|blue)".toRegex()
  var red = 0
  var green = 0
  var blue = 0
  colorRegex.findAll(this).forEach { matchResult ->
    val (value, color) = matchResult.destructured
    when (color) {
      "red" -> red = value.toInt()
      "green" -> green = value.toInt()
      "blue" -> blue = value.toInt()
    }
  }
  return GameSet(red, green, blue)
}

private fun List<GameSet>.isPossible(redCubes: Int, greenCubes: Int, blueCubes: Int): Boolean =
  this.all { it.red <= redCubes } && this.all { it.green <= greenCubes } && this.all { it.blue <= blueCubes }