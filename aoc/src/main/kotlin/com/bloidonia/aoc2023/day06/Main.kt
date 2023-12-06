package com.bloidonia.aoc2023.day06

import com.bloidonia.aoc2023.lines

private const val example = """Time:      7  15   30
Distance:  9  40  200"""

private fun distance(time: Long, best: Long) = (0..time).map { it * (time - it) }.count { it > best }

private fun parse(input: List<String>) = input[0].split(Regex("\\s+")).drop(1).map { it.toLong() }
    .zip(input[1].split(Regex("\\s+")).drop(1).map { it.toLong() })

private fun parse2(input: List<String>) = listOf(
    input[0].split(Regex("\\s+")).drop(1).joinToString("").toLong() to
            input[1].split(Regex("\\s+")).drop(1).joinToString("").toLong()
)

fun main() {
    parse(example.lines()).map { (time, best) -> distance(time, best) }.reduce(Int::times).let(::println)
    parse(lines("/day06.input")).map { (time, best) -> distance(time, best) }.reduce(Int::times).let(::println)

    parse2(example.lines()).map { (time, best) -> distance(time, best) }.reduce(Int::times).let(::println)
    parse2(lines("/day06.input")).map { (time, best) -> distance(time, best) }.reduce(Int::times).let(::println)
}