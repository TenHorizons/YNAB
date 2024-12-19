package com.ynab.ui.shared

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

/**Article explaining the class:
 * https://medium.com/@banmarkovic/how-to-create-currency-amount-input-in-android-jetpack-compose-1bd11ba3b629
 * Github with the class:
 * https://github.com/banmarkovic/CurrencyAmountInput/tree/master/app/src/main/java/com/ban/currencyamountinput*/
class CurrencyAmountInputVisualTransformation(
    private val numberOfDecimals: Int = 2,
    private val isPositiveValue:Boolean
) : VisualTransformation {

    private val symbols = DecimalFormat().decimalFormatSymbols

    override fun filter(text: AnnotatedString): TransformedText {
        val thousandsSeparator = symbols.groupingSeparator
        val decimalSeparator = symbols.decimalSeparator
        val zero = symbols.zeroDigit

        val inputText = text.text

        /*chunked() splits the string from left to right.
        * The last chunk may have less than 3 digits.
        * Therefore, the string is reversed so that
        * the front few digits will be the ones
        * with less than 3 digits
        * Example: 12,345,678*/
        val intPart = inputText
            .dropLast(numberOfDecimals)
            .reversed()
            .chunked(3)
            .joinToString(thousandsSeparator.toString())
            .reversed()
            .ifEmpty {
                zero.toString()
            }

        /*if input text length < numberOfDecimals, fills the from if it with zeros.
        * Example: numberOfDecimals = 2; inputText = "1"
        * Result: "01"
        * This is later combined with intPart to make "xx.01*/
        val fractionPart = inputText.takeLast(numberOfDecimals).let {
            if (it.length != numberOfDecimals) {
                List(numberOfDecimals - it.length) {
                    zero
                }.joinToString("") + it
            } else {
                it
            }
        }

        val formattedNumber = intPart + decimalSeparator + fractionPart

        //might make .toDouble() fail
        val currency = if(isPositiveValue) "RM" else "-RM"

        val displayedText = currency + formattedNumber

        val newText = AnnotatedString(
            text = displayedText,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        /*Offsets cursor based on new text.
        * Example:
        * inputText: 1000
        * displayedText: 10.00*/
        val offsetMapping = FixedCursorOffsetMapping(
            inputLength = inputText.length,
            displayedLength = displayedText.length
        )

        return TransformedText(newText, offsetMapping)
    }

    /*Android Developer Documentation explaining cursor offsetting:
    * https://developer.android.com/reference/kotlin/androidx/compose/ui/text/input/VisualTransformation*/
    private class FixedCursorOffsetMapping(
        private val inputLength: Int,
        private val displayedLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            displayedLength

        override fun transformedToOriginal(offset: Int): Int = inputLength
    }
}