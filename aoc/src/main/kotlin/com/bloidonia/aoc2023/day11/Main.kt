package com.bloidonia.aoc2023.day11

import com.bloidonia.aoc2023.text
import kotlin.math.abs

private const val example = """...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#....."""

private data class Position(val x: Long, val y: Long) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
}

private fun parse(input: String) =
    input.lines().let {
        val width = it.first().length
        val height = it.size
        (width to height) to it.flatMapIndexed { y, line ->
            line.mapIndexed { x, c ->
                when (c) {
                    '#' -> Position(x.toLong(), y.toLong())
                    else -> null
                }
            }
        }.filterNotNull()
    }

private fun expand(input: String, expansion: Long = 1L) {
    val (dimension, galaxy) = parse(input)
    var expanded = galaxy.toMutableList()
    (0 until dimension.first.toLong()).filter { x ->
        galaxy.none { it.x == x }
    }.sortedDescending().let {
        // expand horizontally
        expanded = it.fold(expanded) { points, x ->
            val p = points.filter { it.x < x }.toMutableList()
            p.addAll(points.filter { it.x > x }.map { Position(it.x + expansion, it.y) })
            p
        }
    }
    (0 until dimension.second.toLong()).filter { y ->
        galaxy.none { it.y == y }
    }.sortedDescending().let {
        // expand vertically
        expanded = it.fold(expanded) { points, y ->
            val p = points.filter { it.y < y }.toMutableList()
            p.addAll(points.filter { it.y > y }.map { Position(it.x, it.y + expansion) })
            p
        }
    }

    val distance = { a: Position, b: Position ->
        abs(a.x - b.x) + abs(a.y - b.y)
    }
    (0 until expanded.size).sumOf { i ->
        ((i + 1) until expanded.size).sumOf { j ->
            distance(expanded[i], expanded[j])
        }
    }.let { println("Sum: $it") }
}

fun main() {
    expand(example)
    expand(text("/day11.input"))
    expand(example, 9)
    expand(example, 99)
    expand(text("/day11.input"), 999999)
}