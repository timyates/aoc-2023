package com.bloidonia.aoc2023.day15

import com.bloidonia.aoc2023.text
import kotlin.streams.asSequence

private const val example = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"

private fun score(s: String) = s.chars().asSequence().fold(0L) { acc, n ->
    ((acc + n) * 17).mod(256L)
}

private fun part1(input: String) = input.split(",").map(::score).sum()

private data class Lens(val key: String, val length: Long?) {

    val box by lazy { score(key) }

    companion object {
        private val regex = Regex("""(\w+)(?:[=-](\d*))""")
        operator fun invoke(s: String) = regex.matchEntire(s)!!.let {
            val (key, length) = it.destructured
            Lens(key, length.toLongOrNull())
        }
    }
}

private data class Box(val lenses: List<Lens> = listOf()) {
    fun score() = lenses.mapIndexed { index, lens -> (index + 1) * lens.length!! }
}

private data class Lenses(val boxes: Map<Int, Box> = (0..255).associateWith { Box() }) {

    fun score() = boxes.flatMap { boxEntry ->
        boxEntry.value.score().map { score -> (boxEntry.key + 1) * score }
    }.sum()

    fun apply(lens: Lens): Lenses {
        val box = boxes[lens.box.toInt()]!!
        val lenses = box.lenses.toMutableList()
        if (lens.length == null) {
            lenses.removeAll { it.key == lens.key }
        } else if (lenses.any { it.key == lens.key }) {
            lenses.replaceAll { if (it.key == lens.key) lens else it }
        } else {
            lenses.add(lens)
        }
        return Lenses(boxes + (lens.box.toInt() to Box(lenses)))
    }
}

private fun part2(input: String) = input.split(",")
    .fold(Lenses()) { lenses: Lenses, lens: String ->
        lenses.apply(Lens.invoke(lens))
    }.let { lenses ->
        println(lenses.score())
    }

fun main() {
    println(score("rn"))
    println(part1(example))
    println(part1(text("/day15.input")))
    part2(example)
    part2(text("/day15.input"))
}