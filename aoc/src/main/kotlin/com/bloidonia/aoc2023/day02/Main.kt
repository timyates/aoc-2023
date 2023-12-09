package com.bloidonia.aoc2023.day02

import com.bloidonia.aoc2023.lines

const val example1 = """Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"""

private data class Move(val red: Long, val green: Long, val blue: Long) {
    operator fun plus(other: Move) = Move(red + other.red, green + other.green, blue + other.blue)
    fun power() = red * green * blue
}

private data class Game(val id: Int, val moves: List<Move>) {
    fun max() = moves.reduce { a, b -> Move(maxOf(a.red, b.red), maxOf(a.green, b.green), maxOf(a.blue, b.blue)) }
}

private fun parseGame(s: String) = Regex("""Game (\d+): (.+)""").matchEntire(s)!!.let {
    val (game, moves) = it.destructured
    Game(game.toInt(), moves.split("; ").map { move ->
        Regex("""(\d+) (blue|red|green)""").findAll(move).map { match ->
            val (count, color) = match.destructured
            when (color) {
                "red" -> Move(count.toLong(), 0, 0)
                "green" -> Move(0, count.toLong(), 0)
                "blue" -> Move(0, 0, count.toLong())
                else -> throw Exception("Unknown color: $color")
            }
        }.reduce { a, b -> a + b }
    })
}

private fun part1(game: Game) = game.moves.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }

fun main() {
    val result1 = example1.lines().map(::parseGame).filter(::part1).sumOf { it.id }
    println(result1)

    val lines = lines("/day02.input")
    println("part1: ${lines.map(::parseGame).filter(::part1).sumOf { it.id }}")

    println(example1.lines().map(::parseGame).map(Game::max).map(Move::power).sum())
    println("part2: ${lines.map(::parseGame).map(Game::max).map(Move::power).sum()}")
}