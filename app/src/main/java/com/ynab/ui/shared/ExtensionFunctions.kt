package com.ynab.ui.shared

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import androidx.core.net.ParseException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

/**
 * Currency format but with grouping and two decimals.
 */
fun BigDecimal.toFormattedCurrencyString(locale: Locale = Locale.getDefault()): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)

    formatter.minimumFractionDigits = 2
    formatter.maximumFractionDigits = 2

    // Apply specific symbols if you don't want the default currency symbol
    // or want to customize grouping/decimal separators.
    // For example, to just get commas and dots without a currency symbol:
    // val symbols = formatter.decimalFormatSymbols.apply {
    //     currencySymbol = "" // No currency symbol
    //     // You can also customize groupingSeparator and decimalSeparator if needed
    // }
    // formatter.decimalFormatSymbols = symbols

    return formatter.format(this)
}

/**
 * Non-currency format but with grouping and two decimals.
 * Edit String.currencyStringToBigDecimal(locale: Locale) if plan to use this function.
 */
fun BigDecimal.toGroupedNumericString(locale: Locale = Locale.getDefault()): String {
    val formatter = NumberFormat.getNumberInstance(locale) as DecimalFormat
    formatter.isGroupingUsed = true
    formatter.minimumFractionDigits = 2
    formatter.maximumFractionDigits = 2
    return formatter.format(this)
}


/**
 * Parses a string, potentially containing currency symbols and grouping separators
 * for the given locale, into a BigDecimal.
 *
 * @param locale The locale used to interpret the number format (currency symbol, separators).
 * @return BigDecimal if parsing is successful, null otherwise.
 */
fun String.currencyStringToBigDecimal(locale: Locale = Locale.getDefault()): BigDecimal? {
    // Get a NumberFormat instance for parsing currency for the given locale.
    // It's important to use the same (or compatible) locale that was used for formatting.
    val format = NumberFormat.getCurrencyInstance(locale)
    // For more generic numbers without necessarily a currency symbol, but with grouping:
    // val format = NumberFormat.getNumberInstance(locale)

    return try {
        // Attempt to parse the string.
        // The parse() method returns a Number, which might be a Double or Long
        // depending on the input and implementation.
        val parsedNumber = format.parse(this.trim())

        // Convert the parsed Number to BigDecimal.
        // It's crucial to use `toString()` on parsedNumber before creating BigDecimal
        // if parsedNumber is a Double, to avoid potential precision issues with
        // the Double(double val) constructor of BigDecimal.
        if (parsedNumber != null) {
            BigDecimal(parsedNumber.toString())
        } else {
            null
        }
    } catch (e: ParseException) {
        // If parsing fails (e.g., the string is not a valid number format for the locale),
        // return null or handle the error as appropriate.
        // You might want to log e.localizedMessage for debugging.
        null
    } catch (e: NumberFormatException) {
        // Catch cases where parsedNumber.toString() might not be a valid BigDecimal string,
        // though less likely if format.parse() succeeded.
        null
    }
}

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