package com.bloidonia.aoc2023.day05

import com.bloidonia.aoc2023.lines

private const val example = """seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4"""

private data class Mapping(val source: LongRange, val dx: Long) {
    constructor(line: String) : this(
        line.split(" ").map { it.toLong() }.let { it[1]..<(it[1] + it[2]) },
        line.split(" ").map { it.toLong() }.let { it[0] - it[1] }
    )
}

private data class Mapper(val mappings: List<Mapping>) {
    fun map(input: Long) = (mappings.firstOrNull { input in it.source }?.dx ?: 0).let { input + it }
}

private fun seeds(input: List<String>) = input
    .find { it.startsWith("seeds: ") }!!
    .split(" ")
    .drop(1).map { it.toLong() }

private fun seeds2(input: List<String>) = input
    .find { it.startsWith("seeds: ") }!!
    .split(" ")
    .drop(1)
    .map { it.toLong() }
    .windowed(2, 2)
    .map { it[0]..<(it[0] + it[1]) }

private fun mappings(input: List<String>): List<Mapper> {
    val result = mutableListOf<Mapper>()
    var current = mutableListOf<Mapping>()
    var started = false
    var index = 0
    while (index < input.size) {
        val line = input[index]
        when {
            line.endsWith("map:") -> {
                started = true
            }
            started && line.isEmpty() -> {
                started = false
                if (current.isNotEmpty()) {
                    result.add(Mapper(current))
                    current = mutableListOf()
                }
            }
            started -> {
                current.add(Mapping(line))
            }
        }
        index++
    }
    if (current.isNotEmpty()) {
        result.add(Mapper(current))
    }
    return result
}

private fun part1(input: List<String>) = seeds(input).let { seeds ->
    val mappings = mappings(input)
    seeds.minOfOrNull { seed -> mappings.fold(seed) { a, b -> b.map(a) } }.apply(::println)
}

private fun part2(input: List<String>) = seeds2(input).let { seeds ->
    val mappings = mappings(input)
    var min = Long.MAX_VALUE
    for (range in seeds) {
        for (seed in range) {
            val mapped = mappings.fold(seed) { a, b -> b.map(a) }
            if (mapped < min) {
                min = mapped
            }
        }
    }
    println(min)
}

fun main() {
    part1(example.lines())
    part1(lines("/day05.input"))
    part2(example.lines())
    part2(lines("/day05.input"))
}