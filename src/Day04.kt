import kotlin.math.pow

fun main() {

  fun part1(input: List<String>): Int {
    val cards = input.map { it.toCard() }

    return cards.sumOf {
      2.toDouble().pow(it.winningNumbers.intersect(it.numbers.toSet()).size - 1).toInt()
    }
  }

  fun part2(input: List<String>): Int {
    val cards = input.map { it.toCard() }
    val cardsMap = mutableMapOf<Int, Int>()

    cards.forEachIndexed { index, _ -> cardsMap[index + 1] = 1 }
    cardsMap.printlnDebug()

    cards.forEachIndexed { index, card ->
      "index: $index".printlnDebug()
      val winningNumbers = card.winningNumbers.intersect(card.numbers.toSet()).size
      "winningNumbers: $winningNumbers".printlnDebug()
      val cardsCount = cardsMap[index + 1]
      "cardsCount: $cardsCount".printlnDebug()
      for (winCardNumber in (index + 2)..(index + 1 + winningNumbers)) {
        "winCardNumber: $winCardNumber".printlnDebug()
        cardsMap[winCardNumber] = cardsMap[winCardNumber]!! + cardsCount!!
      }
    }
    cardsMap.printlnDebug()
    return cardsMap.values.sum()
  }

  val testInputFirstStar = readInput("Day04_test")
  check(part1(testInputFirstStar) == 13)

  val input = readInput("Day04")
  println("first star: ${part1(input)}")

  val testInputSecondStar = readInput("Day04_test")
  DEBUG = true
  check(part2(testInputSecondStar) == 30)

//  println("second star: ${part2(input)}")
}

private data class Card(
  val id: Int,
  val winningNumbers: List<Int>,
  val numbers: List<Int>,
)

private fun String.toCard(): Card = Card(
  id = this.substringAfter("Card").substringBefore(":").trim().toInt(),
  winningNumbers = this.substringAfter(":")
    .substringBefore("|")
    .split(" ")
    .filter { it.isNotBlank() }
    .map { it.trim().toInt() },
  numbers = this.substringAfter("|").split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() },
)
