package com.bloidonia.aoc2023.day09

import com.bloidonia.aoc2023.lines

private const val example = """0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45"""

private fun parse(input: String) = input.split(Regex("\\s+")).map { it.toLong() }

// Generate the difference lists, and reverse them
private fun differences(input: String) = generateSequence(parse(input)) { list ->
    list.windowed(2, 1).map { it[1] - it[0] }
}
    .takeWhile { diffs -> diffs.any { it != 0L } }
    .toList()
    .reversed()

// Walk the differences adding the last value to the current
private fun part1(input: String) = differences(input)
    .fold(0L) { acc, n -> acc + n.last() }

// Walk the differences, taking the current away from the first
private fun part2(input: String) = differences(input)
    .fold(0L) { acc, n -> n.first() - acc }

fun main() {
    println(example.lines().map(::part1).sum())
    println("Part 1: ${lines("/day09.input").map(::part1).sum()}")
    println(example.lines().map(::part2))
    println("Part 2: ${lines("/day09.input").map(::part2).sum()}")
}