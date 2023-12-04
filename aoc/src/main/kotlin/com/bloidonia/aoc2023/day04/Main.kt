package com.bloidonia.aoc2023.day04

import com.bloidonia.aoc2023.lines
import kotlin.math.pow

private const val example = """Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"""

data class Card(val id: Int, val winners: List<Int>, val chosen: List<Int>, var count: Int = 1) {

    constructor(line: String) : this(
        line.split(":")[0].split(" ").last().toInt(),
        line.split(":")[1].split("|")[0].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.toList(),
        line.split(":")[1].split("|")[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.toList()
    )

    fun score() = chosen.count { winners.contains(it) }.let { matches ->
        if (matches == 0) 0 else 2.0.pow(matches - 1).toInt()
    }

    fun win(cards: List<Card>) = chosen.count { winners.contains(it) }.let { matches ->
        generateSequence(id) { it + 1 }.take(matches).forEach {
            cards[it].count += count
        }
    }
}

fun main() {
    val exampleCards = example.lines().map(::Card)
    println("Example: ${exampleCards.sumOf(Card::score)}")

    val input = lines("/day04.input").map(::Card).toList()
    println("Part 1: ${input.sumOf(Card::score)}")

    exampleCards.forEach { it.win(exampleCards) }
    println("Example: ${exampleCards.sumOf(Card::count)}")

    input.forEach { it.win(input) }
    println("Part 2: ${input.sumOf(Card::count)}")
}