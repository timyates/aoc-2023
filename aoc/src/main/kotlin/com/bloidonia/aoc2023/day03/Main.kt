package com.bloidonia.aoc2023.day03

import com.bloidonia.aoc2023.lines

private data class Part(val x: IntRange, val y: Int, val value: String)
private data class Symbol(val x: Int, val y: Int, val value: String)

private fun numberLocations(y: Int, line: String) = Regex("""\d+""")
    .findAll(line)
    .map { r -> Part(r.range, y, r.groupValues[0]) }
    .toList()

private fun symbolLocations(y: Int, line: String) = Regex("""[^\d.]""")
    .findAll(line)
    .map { r -> Symbol(r.range.first, y, r.groupValues[0]) }
    .toList()

private fun partAdjacent(part: Part, symbols: List<Symbol>) = symbols.any { symbol ->
    symbol.x in (part.x.first - 1..part.x.last + 1) && symbol.y in (part.y - 1)..(part.y + 1)
}

private fun part1(input: List<String>): Int {
    val numbers = input.flatMapIndexed(::numberLocations)
    val symbols = input.flatMapIndexed(::symbolLocations)

    val filter = numbers.filter { n -> partAdjacent(n, symbols) }

    return filter.sumOf { it.value.toInt() }
}

private fun numberAdjacent(symbol: Symbol, numbers: List<Part>) = numbers.filter { n ->
    (symbol.x - 1..symbol.x + 1).intersect(n.x).isNotEmpty()
            && n.y in (symbol.y - 1..symbol.y + 1)
}

private fun part2(input: List<String>): Long {
    val numbers = input.flatMapIndexed(::numberLocations)
    val symbols = input.flatMapIndexed(::symbolLocations)

    val gears = symbols.filter { it.value == "*" }

    return gears.map { numberAdjacent(it, numbers) }
        .filter { it.size == 2 }
        .sumOf { it.first().value.toLong() * it.last().value.toLong() }
}

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

    part1(example.lines()).let(::println)
    part1(lines).let(::println)

    part2(example.lines()).let(::println)
    part2(lines).let(::println)
}