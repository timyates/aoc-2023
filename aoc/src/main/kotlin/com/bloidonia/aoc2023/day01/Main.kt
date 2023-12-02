package com.bloidonia.aoc2023.day01

import com.bloidonia.aoc2023.lines

val example = """1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet""".lines()

private val example2 = """two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen""".lines()

private fun firstLast(s: String) = s.filter { it.isDigit() }.let {
    "${it.first()}${it.last()}".toInt()
}

private val words = mapOf(
    // Conjoined numbers first
    "oneight" to 18,
    "twone" to 21,
    "threeight" to 38,
    "fiveight" to 58,
    "sevenine" to 79,
    "eightwo" to 82,
    "eighthree" to 83,
    "nineight" to 98,
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

private fun replacement(s: String): String {
    var str = s

    for (word in words.keys) {
        str = str.replace(word, words[word].toString())
    }
    return str
}

fun main() {
    val example = example.sumOf(::firstLast)
    println("example: $example")

    val lines = lines("/day01.input")

    val part1 = lines.sumOf(::firstLast)
    println("$part1")

    println("replacement: ${replacement("one")}")
    val example2 = example2
        .map(::replacement)
        .sumOf(::firstLast)

    println("example2: $example2")

    val part2 = lines.map(::replacement).sumOf(::firstLast)
    println("$part2")
}
