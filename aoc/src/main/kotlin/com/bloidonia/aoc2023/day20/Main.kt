package com.bloidonia.aoc2023.day20

import com.bloidonia.aoc2023.lcm
import com.bloidonia.aoc2023.text
import java.time.LocalTime

private enum class Pulse {
    LOW, HIGH
}

private data class Signal(val source: String, val target: String, val pulse: Pulse) {
    override fun toString() = "$source -${pulse}-> $target"
}

private abstract class Module(val name: String, val targets: List<String>) {
    abstract fun pulse(circuit: Map<String, Module>, pulse: Signal): List<Signal>
}

private class FlipFlop(name: String, targets: List<String>) : Module(name, targets) {
    private var on: Boolean = false

    override fun pulse(circuit: Map<String, Module>, pulse: Signal): List<Signal> {
        if (pulse.pulse == Pulse.LOW) {
            on = !on
            return targets.map { Signal(name, it, if (on) Pulse.HIGH else Pulse.LOW) }
        }
        return listOf()
    }

    override fun toString() = "FlipFlop(name='$name', targets=$targets)"
}

private class Broadcast(targets: List<String>) : Module("broadcast", targets) {

    override fun pulse(circuit: Map<String, Module>, pulse: Signal): List<Signal> {
        return targets.map { Signal("broadcaster", it, pulse.pulse) }
    }

    override fun toString() = "Broadcast(targets=$targets)"
}

private class Conjunction(name: String, targets: List<String>, val inputs: MutableMap<String, Pulse> = mutableMapOf()) :
    Module(name, targets) {

    fun setInputs(names: List<String>) {
        names.forEach {
            this.inputs[it] = Pulse.LOW
        }
    }

    override fun pulse(circuit: Map<String, Module>, pulse: Signal): List<Signal> {
        inputs[pulse.source] = pulse.pulse
        if (inputs.values.all { it == Pulse.HIGH }) {
            return targets.map { Signal(name, it, Pulse.LOW) }
        }
        return targets.map { Signal(name, it, Pulse.HIGH) }
    }

    override fun toString() = "Conjunction(name='$name', targets=$targets, inputs=$inputs)"
}

private fun parse(input: String) = input.lines().associate {
    val (name, targets) = it.split(" -> ")
    val targetNames = targets.split(", ")
    when {
        name == "broadcaster" -> name to Broadcast(targetNames)
        name.startsWith("%") -> name.drop(1) to FlipFlop(name.drop(1), targetNames)
        else -> name.drop(1) to Conjunction(name.drop(1), targetNames)
    }
}.let { circuit ->
    circuit.values
        .filterIsInstance<Conjunction>()
        .forEach { c -> c.setInputs(circuit.values.filter { it.targets.contains(c.name) }.map { it.name }) }
    circuit
}

private fun part1(circuit: Map<String, Module>): Map<Pulse, Int> {
    val totals = mutableMapOf(
        Pulse.LOW to 0,
        Pulse.HIGH to 0
    )

    for (i in 1..1000) {
        val signals = mutableListOf(Signal("button", "broadcaster", Pulse.LOW))
        while (signals.isNotEmpty()) {
            signals.forEach { signal ->
                totals[signal.pulse] = totals[signal.pulse]!! + 1
            }

            signals.flatMap { signal ->
                circuit[signal.target]?.pulse(circuit, signal) ?: listOf()
            }.let {
                signals.clear()
                signals.addAll(it)
            }
        }
    }
    return totals
}

private fun emitsHigh(circuit: Map<String, Module>, name: String): Long {
    var i = 1L
    while (true) {
        val signals = mutableListOf(Signal("button", "broadcaster", Pulse.LOW))
        while (signals.isNotEmpty()) {

            if (signals.any { it.source == name && it.pulse == Pulse.HIGH }) {
                return i
            }

            signals.flatMap { signal ->
                circuit[signal.target]?.pulse(circuit, signal) ?: listOf()
            }.let {
                signals.clear()
                signals.addAll(it)
            }
        }
        if (++i % 10000000 == 0L) println("${LocalTime.now()} $i")
    }
}

private const val example = """broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output"""

fun main() {
    val example = parse(example)
    part1(example).let {
        println("Example Total: ${it[Pulse.LOW]!! * it[Pulse.HIGH]!!} (Low: ${it[Pulse.LOW]}, High: ${it[Pulse.HIGH]})")
    }

    part1(parse(text("/day20.input"))).let {
        println("Part 1 Total: ${it[Pulse.LOW]!! * it[Pulse.HIGH]!!} (Low: ${it[Pulse.LOW]}, High: ${it[Pulse.HIGH]})")
    }
    println((parse(text("/day20.input")).values.first { it.targets.contains("rx") } as Conjunction)
        .inputs
        .keys)

    // It's the LCM of the steps until the Conjunctions that lead to it fire a high signal
    // Once they all fire HIGH at the same time, then a HIGH is sent to the rx node
    (parse(text("/day20.input")).values.first { it.targets.contains("rx") } as Conjunction)
        .inputs
        .keys
        .map { name ->
            emitsHigh(parse(text("/day20.input")), name)
        }
        .toLongArray()
        .let { println("Part 2: ${lcm(*it)}") }
}