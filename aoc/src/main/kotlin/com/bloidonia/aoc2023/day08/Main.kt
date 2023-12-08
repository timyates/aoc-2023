package com.bloidonia.aoc2023.day08

import com.bloidonia.aoc2023.lcm
import com.bloidonia.aoc2023.lines
import com.bloidonia.aoc2023.repeat

private const val example = """RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)"""

private const val example2 = """LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)"""

private const val example3 = """LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)"""

private fun moves(input: String) = input.map { if (it == 'R') 1 else 0 }.asSequence()
private data class Node(val next: List<String>) {
    constructor(input: String) : this(Regex(""".+ = \(([^,]+), ([^)]+)\)""").matchEntire(input).let {
        val (a, b) = it!!.destructured
        listOf(a, b)
    })
}

private fun parse(input: List<String>) = input
    .drop(2)
    .associate { i ->
        i.split(Regex("\\s+"))[0] to Node(i)
    }

private fun part1(input: List<String>): Int {
    val moves = moves(input[0])
    val network = parse(input)
    return solve("AAA", moves, network) { it == "ZZZ" }
}

private fun solve(start: String, moves: Sequence<Int>, network: Map<String, Node>, success: (String) -> Boolean) = moves
    .repeat()
    .runningFold(start) { acc, move ->
        network[acc]!!.next[move]
    }
    .withIndex()
    .find { success(it.value) }!!.index

private fun part2(input: List<String>): Long {
    val moves = moves(input[0])
    val network = parse(input)
    // Each node has a cyclic path to itself, so we can just find the LCM of the cycle lengths
    val numbers = network.keys.filter { it.endsWith("A") }
        .map { startNode ->
            val stepsRequired = solve(startNode, moves, network) { it.endsWith("Z") }.toLong()
            println("$startNode requires $stepsRequired steps")
            stepsRequired
        }
        .toLongArray()

    return lcm(*numbers)
}

fun main() {
    println("Example 1: ${part1(example.lines())}")
    println("Example 2: ${part1(example2.lines())}")
    println("Part 1: ${part1(lines("/day08.input"))}")
    println("Example 3: ${part2(example3.lines())}")
    println("Part 2: ${part2(lines("/day08.input"))}")
}