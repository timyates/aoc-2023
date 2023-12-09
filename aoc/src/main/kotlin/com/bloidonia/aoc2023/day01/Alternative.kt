package com.bloidonia.aoc2023.day01

private val mapping = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

private fun part1(s: String) = s.filter { it.isDigit() }.let {
    "${it.first()}${it.last()}".toInt()
}

private fun part2(s: String) = Regex("(?=(\\d|one|two|three|four|five|six|seven|eight|nine))").let { regex ->
    regex.findAll(s).map { it.groupValues[1] }.map { mapping[it] ?: it.toInt() }.let {
        "${it.first()}${it.last()}".toInt()
    }
}

fun main() {
    val lines = object {}.javaClass.getResource("/day01.input")!!.readText().lines()

    lines.map(::part1).sum().let(::println)
    lines.map(::part2).sum().let(::println)
}