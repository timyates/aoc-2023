package com.bloidonia.aoc2023.graph

import java.util.PriorityQueue

typealias Neighbours<Vertex> = (Vertex) -> List<Vertex>
typealias End<Vertex> = (Vertex) -> Boolean
typealias Cost<Vertex> = (Vertex, Vertex) -> Long
typealias Fudge<Vertex> = (Vertex) -> Long

data class Seen<Vertex>(val cost: Long, val prev: Vertex?)
private data class Score<Vertex>(
    val vertex: Vertex,
    val score: Long,
    val fudge: Long
) : Comparable<Score<Vertex>> {
    override fun compareTo(other: Score<Vertex>): Int =
        (score + fudge).compareTo(other.score + other.fudge)
}

class Result<Vertex>(
    val start: Vertex,
    private val end: Vertex?,
    private val result: Map<Vertex, Seen<Vertex>>
) {
    private fun getScore(vertex: Vertex) = result[vertex]?.cost
    fun getScore() = end?.let { getScore(it) }
}

fun <Vertex> shortestPath(
    start: Vertex,
    endFunction: End<Vertex>,
    neighbours: Neighbours<Vertex>,
    cost: Cost<Vertex> = { _, _ -> 1 },
    fudge: Fudge<Vertex> = { 0 }
): Result<Vertex> {
    val toVisit = PriorityQueue(listOf(Score(start, 0, fudge(start))))
    val seenPoints: MutableMap<Vertex, Seen<Vertex>> = mutableMapOf(start to Seen(0, null))

    var last: Vertex? = null
    while (last == null) {
        if (toVisit.isEmpty()) {
            // No path
            return Result(start, null, seenPoints)
        }

        val (current, currentScore) = toVisit.remove()
        last = if (endFunction(current)) current else null

        val nextPoints = neighbours(current)
            .filter { it !in seenPoints }
            .map { next -> Score(next, currentScore + cost(current, next), fudge(next)) }

        toVisit.addAll(nextPoints)
        seenPoints.putAll(nextPoints.associate { it.vertex to Seen(it.score, current) })
    }

    return Result(start, last, seenPoints)
}
