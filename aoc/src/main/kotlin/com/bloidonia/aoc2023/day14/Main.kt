package com.bloidonia.aoc2023.day14

import com.bloidonia.aoc2023.text
import java.time.LocalTime

private const val example = """O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#...."""

private data class Position(var x: Int, var y: Int)
private data class Block(val position: Position, val static: Boolean)

private fun height(input: String) = input.lines().size
private fun width(input: String) = input.lines().first().length
private fun world(input: String) = input.lines().flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
        when (c) {
            '#' -> Block(Position(x, y), true)
            'O' -> Block(Position(x, y), false)
            else -> null
        }
    }
}.filterNotNull()

private fun printWorld(world: List<Block>, width: Int, height: Int) {
    (0 until height).forEach { y ->
        (0 until width).forEach { x ->
            val block = world.find { it.position.x == x && it.position.y == y }
            print(if (block != null) if (block.static) '#' else 'O' else '.')
        }
        println()
    }
}

private enum class Direction {
    NORTH, WEST, SOUTH, EAST
}

private fun lean(world: List<Block>, height: Int, width: Int, loops: Long): List<Block> {
    generateSequence(0L to Direction.NORTH) {
        (it.first + 1) to when (it.second) {
            Direction.NORTH -> Direction.WEST
            Direction.WEST -> Direction.SOUTH
            Direction.SOUTH -> Direction.EAST
            Direction.EAST -> Direction.NORTH
        }
    }.takeWhile { it.first < loops }
        .forEach { stepAndDirection ->
            val walk = when (stepAndDirection.second) {
                Direction.NORTH -> 0..<height
                Direction.SOUTH -> (height - 1) downTo 0
                Direction.EAST -> (width - 1) downTo 0
                Direction.WEST -> 0..<width
            }
            val filter = when (stepAndDirection.second) {
                Direction.NORTH, Direction.SOUTH -> { y, it -> it.position.y == y }
                Direction.EAST, Direction.WEST -> { y: Int, it: Block -> it.position.x == y }
            }
            val calc = when (stepAndDirection.second) {
                Direction.NORTH -> { block: Block ->
                    block.position.y = (world.filter { it.position.x == block.position.x && it.position.y < block.position.y }
                        .maxOfOrNull { it.position.y } ?: -1) + 1
                }

                Direction.SOUTH -> { block ->
                    block.position.y = (world.filter { it.position.x == block.position.x && it.position.y > block.position.y }
                        .minOfOrNull { it.position.y } ?: height) - 1
                }

                Direction.WEST -> { block ->
                    block.position.x = (world.filter { it.position.y == block.position.y && it.position.x < block.position.x }
                        .maxOfOrNull { it.position.x } ?: -1) + 1
                }

                Direction.EAST -> { block ->
                    block.position.x = (world.filter { it.position.y == block.position.y && it.position.x > block.position.x }
                        .minOfOrNull { it.position.x } ?: width) - 1
                }
            }
            walk.forEach { pos ->
                world.filter { filter(pos, it) }
                    .forEach { block ->
                        if (!block.static) calc(block)
                    }
            }
            if (stepAndDirection.first.mod(4) == 2) println("${stepAndDirection.first.mod(4)} : ${score(world, height)}")
        }

    return world
}

private fun score(world: List<Block>, height: Int) = (0..<height).sumOf { y ->
    world.filter { it.position.y == y }.count { !it.static } * (height - y)
}

private fun part1(input: String) = score(lean(world(input), height(input), width(input), 1), height(input))
private fun part2(input: String): Int {
    val world = world(input)
    val ret = score(lean(world, height(input), width(input), 1000000000 * 4L), height(input))
    return ret
}

fun main() {
    println(part1(example))
    println(part1(text("/day14.input")))
    println(LocalTime.now())
//    println(part2(example))
    println(LocalTime.now())
    println(part2(text("/day14.input")))
    println(LocalTime.now())
}