package com.bloidonia.aoc2023.day03

import com.bloidonia.aoc2023.lines

private data class Number(val x: IntRange, val y: Int, val value: String)
private data class Symbol(val x: Int, val y: Int, val value: String)
private data class Board(val numbers: List<Number>, val symbols: List<Symbol>) {
    constructor(input: List<String>) : this(
        input.flatMapIndexed(::numberLocations),
        input.flatMapIndexed(::symbolLocations)
    )
}

private fun numberLocations(y: Int, line: String) = Regex("""\d+""")
    .findAll(line)
    .map { r -> Number(r.range, y, r.groupValues[0]) }
    .toList()

private fun symbolLocations(y: Int, line: String) = Regex("""[^\d.]""")
    .findAll(line)
    .map { r -> Symbol(r.range.first, y, r.groupValues[0]) }
    .toList()

private fun partAdjacent(number: Number, symbols: List<Symbol>) = symbols.any { symbol ->
    symbol.x in (number.x.first - 1..number.x.last + 1)
            && symbol.y in (number.y - 1)..(number.y + 1)
}

private fun numberAdjacent(symbol: Symbol, numbers: List<Number>) = numbers.filter { n ->
    (symbol.x - 1..symbol.x + 1).intersect(n.x).isNotEmpty()
            && n.y in (symbol.y - 1..symbol.y + 1)
}

private fun part1(input: Board) = input.numbers
    .filter { n -> partAdjacent(n, input.symbols) }
    .sumOf { it.value.toInt() }

private fun part2(input: Board) = input.symbols
    .filter { it.value == "*" }
    .map { numberAdjacent(it, input.numbers) }
    .filter { it.size == 2 }
    .sumOf { it.first().value.toLong() * it.last().value.toLong() }

private const val example = """467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598.."""

fun main() {
    val lines = lines("/day03.input")
    val example = Board(example.lines())
    val day03 = Board(lines)

    part1(example).let(::println)
    part1(day03).let(::println)

    part2(example).let(::println)
    part2(day03).let(::println)
}