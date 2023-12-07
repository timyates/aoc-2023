package com.bloidonia.aoc2023.day07

import com.bloidonia.aoc2023.lines
import java.lang.Math.pow

private const val example = """32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483"""

private val part1CardOrder = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A")
private val part2CardOrder = listOf("J", "1", "2", "3", "4", "5", "6", "7", "8", "9", "T", "Q", "K", "A")

data class Hand(val cards: Cards, val bet: Long) {
    constructor(line: String, part1: Boolean) : this(
        Cards(line.split(Regex("\\s+"))[0].toList().map { "$it" }, part1),
        line.split(Regex("\\s+"))[1].toLong(),
    )
}

data class Cards(
    val cards: List<String>,
    val part1: Boolean,
    val rank: Int = (if (part1) cards else cards.filter { it != "J" }).groupBy { it }.values.map { it.size }.sortedDescending().let { counts ->
        val jokers = if (part1) 0 else cards.count { it == "J" }
        when {
            jokers == 5 || counts[0] + jokers == 5 -> 7 // Five of a kind
            counts[0] + jokers == 4 -> 6 // Four of a kind
            counts[0] + jokers == 3 && counts[1] == 2 -> 5 // Full house
            counts[0] + jokers == 3 -> 4 // Three of a kind
            counts[0] == 2 && counts[1] == 2 -> 3 // Two pair -- can't be got with Jokers
            counts[0] + jokers == 2 -> 2 // One pair
            else -> 1 // High card
        }
    },
    // Score is a base 14 number
    val score: Double = cards.reversed().mapIndexed { index, card ->
        if (part1)
            part1CardOrder.indexOf(card) * pow(part1CardOrder.size.toDouble(), index.toDouble())
        else
            part2CardOrder.indexOf(card) * pow(part2CardOrder.size.toDouble(), index.toDouble())
    }.sum()
)

private fun handComparator(a: Hand, b: Hand) = a.cards.rank.compareTo(b.cards.rank).let { if (it != 0) it else a.cards.score.compareTo(b.cards.score) }

private fun score(hands: List<Hand>) = hands.sortedWith(::handComparator)
    .mapIndexed { idx, g -> (idx + 1) * g.bet }
    .sum()

private fun part1(input: List<String>) = score(input.map { Hand(it, true) })
private fun part2(input: List<String>) = score(input.map { Hand(it, false) })

fun main() {
    println("Example: ${part1(example.lines())}")
    println("Part 1: ${part1(lines("/day07.input"))}")
    println("Example: ${part2(example.lines())}")
    println("Part 2: ${part2(lines("/day07.input"))}")
}