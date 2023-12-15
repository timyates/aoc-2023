package com.bloidonia.aoc2023.day10

import com.bloidonia.aoc2023.text

private const val example = """-L|F7
7S-7|
L|7||
-L-J|
L|-JF"""

private const val example2 = """7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ"""

private const val example3 = """...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
..........."""

private const val example4 = """.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ..."""

private data class Position(val x: Int, val y: Int) {
}

private data class Tile(val position: Position, val exits: List<Position>, var distance: Long = 0) {
}

private fun parse(input: String) = input.lines().flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
        Position(x, y) to when (c) {
            '-' -> Tile(Position(x, y), listOf(Position(x - 1, y), Position(x + 1, y)))
            '|' -> Tile(Position(x, y), listOf(Position(x, y - 1), Position(x, y + 1)))
            'L' -> Tile(Position(x, y), listOf(Position(x, y - 1), Position(x + 1, y)))
            'J' -> Tile(Position(x, y), listOf(Position(x, y - 1), Position(x - 1, y)))
            'F' -> Tile(Position(x, y), listOf(Position(x, y + 1), Position(x + 1, y)))
            '7' -> Tile(Position(x, y), listOf(Position(x, y + 1), Position(x - 1, y)))
            'S' -> Tile(Position(x, y), listOf())
            else -> Tile(Position(x, y), listOf(Position(x, y)))
        }
    }
}.toMap().toMutableMap()

private fun process(input: String) = parse(input).let { map ->
    // Find the start and fix the exits
    val startPosition = map.firstNotNullOf { (pos, tile) -> if (tile.exits.isEmpty()) pos else null }
    map[startPosition] = Tile(startPosition, map.filter { (_, tile) -> tile.exits.contains(startPosition) }.keys.toList())

    val seen = mutableSetOf(startPosition)
    var tiles = map[startPosition]?.exits!!
    var distance = 1L
    while (tiles.isNotEmpty()) {
        tiles = tiles.flatMap {
            val tile = map[it]!!
            seen.add(it)
            tile.distance = distance
            tile.exits.filter { exit -> !seen.contains(exit) }
        }
        distance++
    }
    println("Max distance along loop: ${map.values.maxByOrNull { it.distance }!!.distance}")
}

fun main() {
    process(example)
    process(example2)
    process(example3)
    process(example4)
    process(text("/day10.input"))
}