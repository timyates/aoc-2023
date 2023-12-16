package com.bloidonia.aoc2023.day16

import com.bloidonia.aoc2023.text

private const val example = """.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|...."""

private data class Position(var x: Int, var y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    fun move(direction: Direction) = this + direction.position
}

private data class Block(val position: Position, val type: String)
private data class Ray(val position: Position, val direction: Direction) {
    fun interact(block: Block?) = when {
        block?.type == "/" -> listOf(
            when (direction) {
                Direction.UP -> Ray(position, Direction.RIGHT)
                Direction.DOWN -> Ray(position, Direction.LEFT)
                Direction.LEFT -> Ray(position, Direction.DOWN)
                Direction.RIGHT -> Ray(position, Direction.UP)
            }
        )

        block?.type == "\\" -> listOf(
            when (direction) {
                Direction.UP -> Ray(position, Direction.LEFT)
                Direction.DOWN -> Ray(position, Direction.RIGHT)
                Direction.LEFT -> Ray(position, Direction.UP)
                Direction.RIGHT -> Ray(position, Direction.DOWN)
            }
        )

        block?.type == "-" && (direction == Direction.UP || direction == Direction.DOWN) -> listOf(
            Ray(position, Direction.LEFT),
            Ray(position, Direction.RIGHT)
        )

        block?.type == "|" && (direction == Direction.LEFT || direction == Direction.RIGHT) -> listOf(
            Ray(position, Direction.UP),
            Ray(position, Direction.DOWN)
        )

        else -> listOf(this)
    }
}

private enum class Direction(val position: Position) {
    UP(Position(0, -1)),
    DOWN(Position(0, 1)),
    LEFT(Position(-1, 0)),
    RIGHT(Position(1, 0));
}

private fun walk(input: List<String>, width: Int, height: Int, initial: Ray) = input.let { lines ->
    val world = lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, c ->
            when (c) {
                '.' -> null
                else -> Position(x, y) to Block(Position(x, y), "$c")
            }
        }
    }.filterNotNull().toMap()
    val visited = mutableSetOf<Ray>()
    var rays = listOf(initial)
    while (rays.isNotEmpty()) {
        visited.addAll(rays)
        rays = rays.flatMap { it.interact(world[it.position]) }
            .map { Ray(it.position.move(it.direction), it.direction) }
            .filter { it.position.x in 0 until width && it.position.y in 0 until height }
            .filter { it !in visited }
    }
    visited.map { it.position }.distinct().size
}

private fun part1(input: String) = input.lines().let {
    val width = it.first().length
    val height = it.size
    walk(it, width, height, Ray(Position(0, 0), Direction.RIGHT))
}

private fun part2(input: String) = input.lines().let { lines ->
    val width = lines.first().length
    val height = lines.size

    val maxLR = (0 until height).flatMap { y ->
        listOf(
            walk(lines, width, height, Ray(Position(0, y), Direction.RIGHT)),
            walk(lines, width, height, Ray(Position(width - 1, y), Direction.LEFT))
        )
    }.max()
    val maxUD = (0 until width).flatMap { x ->
        listOf(
            walk(lines, width, height, Ray(Position(x, 0), Direction.DOWN)),
            walk(lines, width, height, Ray(Position(x, height - 1), Direction.UP))
        )
    }.max()
    maxOf(maxLR, maxUD)
}

fun main() {
    println(part1(example))
    println(part1(text("/day16.input")))
    println(part2(example))
    println(part2(text("/day16.input")))
}
