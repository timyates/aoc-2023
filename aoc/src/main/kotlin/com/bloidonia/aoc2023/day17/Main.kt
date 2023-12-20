package com.bloidonia.aoc2023.day17

import com.bloidonia.aoc2023.graph.End
import com.bloidonia.aoc2023.graph.Neighbours
import com.bloidonia.aoc2023.graph.shortestPath
import com.bloidonia.aoc2023.text

private const val example = """2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533"""

private const val example2 = """111111111111
999999999991
999999999991
999999999991
999999999991"""

private data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
}

private enum class Direction(val position: Position) {

    UP(Position(0, -1)),
    DOWN(Position(0, 1)),
    LEFT(Position(-1, 0)),
    RIGHT(Position(1, 0));

    fun left() = when (this) {
        UP -> LEFT
        LEFT -> DOWN
        DOWN -> RIGHT
        RIGHT -> UP
    }

    fun right() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }
}

private data class Cart(val position: Position, val direction: Direction, val steps: Int) {
    fun next() = buildList {
        if (steps < 3) {
            add(Cart(position + direction.position, direction, steps + 1))
        }
        add(Cart(position + direction.left().position, direction.left(), 1))
        add(Cart(position + direction.right().position, direction.right(), 1))
    }

    fun ultraNext() = buildList {
        if (steps < 10) {
            add(Cart(position + direction.position, direction, steps + 1))
        }
        // Can't turn until 4 steps -- OR AT STARTING POSITION
        if (steps >= 4 || position == Position(0, 0)) {
            add(Cart(position + direction.left().position, direction.left(), 1))
            add(Cart(position + direction.right().position, direction.right(), 1))
        }
    }
}

private fun walk(
    input: String,
    nextFn: Neighbours<Cart>,
    endFn: End<Cart>
): Long? {
    val map = input.lines().withIndex()
        .flatMap { line -> line.value.mapIndexed { x, it -> Position(x, line.index) to it.digitToInt().toLong() } }
        .toMap()
    // Start going right, as next can cover both the right and down direction
    val start = Cart(Position(0, 0), Direction.RIGHT, 0)
    val path = shortestPath(start, endFn, nextFn, { _, (point) -> map[point]!! })
    return path.getScore()
}

private fun part1(input: String): Long? {
    val (width, height) = input.lines().let { it[0].length to it.size }
    return walk(
        input,
        { it.next().filter { n -> n.position.x in 0..<width && n.position.y in 0..<height } },
        { (p, _) -> p == Position(width - 1, height - 1) }
    )
}

private fun part2(input: String): Long? {
    val (width, height) = input.lines().let { it[0].length to it.size }
    return walk(
        input,
        { it.ultraNext().filter { n -> n.position.x in 0..<width && n.position.y in 0..<height } },
        // We need to have done at least 4 steps to get in the end position
        { (p, _, steps) -> p == Position(width - 1, height - 1) && steps >= 4 }
    )
}

fun main() {
    println("Example 1: ${part1(example)}")
    println("Part 1: ${part1(text("/day17.input"))}")
    println("Example 2a: ${part2(example)}")
    println("Example 2b: ${part2(example2)}")
    println("Part 2: ${part2(text("/day17.input"))}")
}