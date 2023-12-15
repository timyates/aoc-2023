package com.bloidonia.aoc2023

fun lines(resource: String) = text(resource).lines()
fun text(resource: String) = object {}.javaClass.getResource(resource)!!.readText()

fun gcd(x: Long, y: Long): Long {
    return if (y == 0L) x else gcd(y, x % y)
}

fun gcd(vararg numbers: Long): Long {
    return numbers.fold(0L) { x: Long, y: Long -> gcd(x, y) }
}

fun lcm(vararg numbers: Long): Long {
    return numbers.fold(1L) { x: Long, y: Long -> x * (y / gcd(x, y)) }
}

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

private fun check(fn: (LongArray) -> Long, expected: Long, vararg numbers: Long) = fn(numbers)
    .apply { if (this != expected) throw Exception("Expected $expected, got $this") }

fun main() {
    check(::lcm, 12, 2, 3, 4)
    check(::lcm, 60, 2, 3, 4, 5)
    check(::lcm, 60, 2, 3, 4, 5, 6)
    check(::lcm, 420, 2, 3, 4, 5, 6, 7)

    check(::gcd, 60, 420, 240, 60)
    check(::gcd, 1, 420, 240, 59)
}