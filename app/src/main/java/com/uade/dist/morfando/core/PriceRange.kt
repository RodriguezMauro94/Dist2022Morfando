package com.uade.dist.morfando.core

val priceRange = mapOf(
    Pair(1, "$-$$"),
    Pair(2, "$$-$$$"),
    Pair(3, "$$$-$$$$"),
    Pair(4, "$$$$-$$$$$"),
)

fun Int.toPriceRange(): String {
    return when (this) {
        1, 2, 3 -> priceRange[this]!!
        else -> priceRange[4]!!
    }
}