package com.ynab.ui.shared

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.displayTwoDecimal(): BigDecimal =
    this.setScale(2, RoundingMode.HALF_UP)

fun BigDecimal.isZero(): Boolean =
    this.compareTo(BigDecimal.ZERO) == 0

fun BigDecimal.isLessThanZero(): Boolean =
    this.compareTo(BigDecimal.ZERO) == -1

fun String.currencyStringToBigDecimal(): BigDecimal {
    if (this.toIntOrNull() == null) throw IllegalArgumentException("String.currencyStringToBigDecimal() only accepts digits as input")
    val balanceAsString: String = if (this == "" || startsWith("0")) "0"
    else {
        if (this.length < 2)
            "0." + List(2 - this.length) { 0 }.joinToString("") + this
        else
            this.dropLast(2) + "." + this.takeLast(2)
    }
    return BigDecimal(balanceAsString).setScale(2, RoundingMode.HALF_UP)
}

fun BigDecimal.toCurrencyString(): String {
    val prefix =
        if (this.isLessThanZero()) "-RM"
        else "RM"
    return prefix + this.abs().displayTwoDecimal().toString()
}