package com.bloidonia.aoc2023.day19

import com.bloidonia.aoc2023.text

private const val example = """px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}"""

private typealias Condition = (Part) -> String

private data class Rule(val fn: Condition) {

    fun process(part: Part) = fn(part)

    companion object {
        fun parse(input: String): Pair<String, Rule> {
            Regex("""([a-z]+)\{([^}]+)}""").matchEntire(input)!!.let {
                val (key, value) = it.destructured
                val values = value.split(",")
                var fn = { part: Part -> values.last() }
                values.dropLast(1).reversed().forEach { condition ->
                    val (k, op, v, then) = Regex("""([a-z])([<>=])(\d+):([A-Za-z]+)""").matchEntire(condition)!!.destructured
                    val oldFn = fn
                    fn = when (op) {
                        "<" -> { part: Part -> if (part.qualities[k]!! < v.toLong()) then else oldFn.invoke(part) }
                        ">" -> { part: Part -> if (part.qualities[k]!! > v.toLong()) then else oldFn.invoke(part) }
                        "=" -> { part: Part -> if (part.qualities[k]!! == v.toLong()) then else oldFn.invoke(part) }
                        else -> throw Exception("Unknown operator: $op")
                    }
                }
                return key to Rule(fn)
            }
        }
    }
}

private data class Part(val qualities: Map<String, Long>) {
    fun score() = qualities.values.sum().toLong()

    constructor(input: String) : this(input
        .removePrefix("{")
        .removeSuffix("}")
        .split(",")
        .associate {
            val (k, v) = it.split("=")
            k to v.toLong()
        })
}

private fun part1(input: String) {
    input.split("\n\n").let { (rules, parts) ->
        val parsedRules = rules.split("\n").map(Rule::parse).toMap()
        val parsedParts = parts.split("\n").map(::Part)

        var score = 0L
        parsedParts.forEach { part ->
            var key = "in"
            while (key != "A" && key != "R") {
                key = parsedRules[key]!!.process(part)
            }
            if (key == "A") {
                score += part.score()
            }
        }
        println(score)
    }
}

fun main() {
    part1(example)
    part1(text("/day19.input"))
}