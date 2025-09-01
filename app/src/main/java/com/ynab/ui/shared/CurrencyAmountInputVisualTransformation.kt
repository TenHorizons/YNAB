package com.ynab.ui.shared

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**Article explaining the class:
 * https://medium.com/@banmarkovic/how-to-create-currency-amount-input-in-android-jetpack-compose-1bd11ba3b629
 * Github with the class:
 * https://github.com/banmarkovic/CurrencyAmountInput/tree/master/app/src/main/java/com/ban/currencyamountinput*/
class CurrencyAmountInputVisualTransformation(
    private val locale: Locale = Locale.getDefault(),
    private val fixedDecimalDigits: Int = Currency.getInstance(locale).defaultFractionDigits
) : VisualTransformation {

    private val numberFormat: DecimalFormat =
        (NumberFormat.getCurrencyInstance(locale) as DecimalFormat)

    // We need the raw symbols for parsing and building the display string carefully
    private val decimalSeparator: Char = numberFormat.decimalFormatSymbols.decimalSeparator
    private val groupingSeparator: Char = numberFormat.decimalFormatSymbols.groupingSeparator
    private val currencySymbol: String = numberFormat.decimalFormatSymbols.currencySymbol

    override fun filter(text: AnnotatedString): TransformedText {

        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(
                AnnotatedString(formatToCurrency(0L)), // Show "RM0.00" or "$0.00"
                OffsetMapping.Identity
            )
        }

        // Ensure only digits are processed, helpful if input source isn't perfectly clean
        val digitsOnly = originalText.filter { it.isDigit() }
        if (digitsOnly.isEmpty()) {
            return TransformedText(
                AnnotatedString(formatToCurrency(0L)),
                OffsetMapping.Identity
            )
        }

        val amountInSmallestUnit = digitsOnly.toLongOrNull() ?: 0L
        val formattedString = formatToCurrency(amountInSmallestUnit)

        return TransformedText(
            text = AnnotatedString(formattedString),
            offsetMapping = ForceCursorToEndOffsetMapping(
                transformedTextLength = formattedString.length,
                originalTextLength = digitsOnly.length
            )
        )
    }


    private fun formatToCurrency(amountInSmallestUnit: Long): String {
        // We need to construct the display string carefully to match DecimalFormat's output
        // without directly using its format method on a pre-divided number,
        // as that can mess with how we want to handle the input (raw cents/smallest unit).

        val majorUnits = amountInSmallestUnit / powerOfTen(fixedDecimalDigits)
        val minorUnits = amountInSmallestUnit % powerOfTen(fixedDecimalDigits)

        // Format major part with grouping
        val majorFormat = (NumberFormat.getNumberInstance(locale) as DecimalFormat).apply {
            isGroupingUsed = true
            minimumFractionDigits = 0 // No decimals for the major part here
            maximumFractionDigits = 0
        }
        val formattedMajorPart = majorFormat.format(majorUnits)

        // Format minor part, padding with leading zeros if necessary
        val formattedMinorPart = minorUnits.toString().padStart(fixedDecimalDigits, '0')

        return if (fixedDecimalDigits > 0) {
            "$currencySymbol$formattedMajorPart$decimalSeparator$formattedMinorPart"
        } else {
            "$currencySymbol$formattedMajorPart" // For currencies with no decimals (e.g., JPY)
        }
    }

    private fun powerOfTen(exponent: Int): Long {
        var result = 1L
        repeat(exponent) { result *= 10 }
        return result
    }
}


/**
 * An OffsetMapping that always places the cursor at the end of the transformed text.
 * The original offset is largely ignored for transformedToOriginal, and originalToTransformed
 * always returns the length of the transformed text.
 *
 * @param transformedTextLength The length of the fully formatted (transformed) string.
 * @param originalTextLength The length of the original, unformatted text.
 */
class ForceCursorToEndOffsetMapping(
    private val transformedTextLength: Int,
    private val originalTextLength: Int
) : OffsetMapping {

    /**
     * Maps an offset from the original text to an offset in the transformed text.
     * Always returns the end of the transformed text.
     */
    override fun originalToTransformed(offset: Int): Int {
        return transformedTextLength
    }

    /**
     * Maps an offset from the transformed text to an offset in the original text.
     * Always returns the end of the original text, as we assume any interaction
     * with the transformed text implies editing at the end of the conceptual raw input.
     */
    override fun transformedToOriginal(offset: Int): Int {
        return originalTextLength
    }
}