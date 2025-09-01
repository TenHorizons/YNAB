package com.ynab.ui.shared

import androidx.compose.ui.text.AnnotatedString
import com.google.common.truth.Truth.assertThat // Using Google Truth for assertions
import org.junit.Test
import java.util.Currency
import java.util.Locale

class CurrencyAmountInputVisualTransformationTest {

    // --- Test Data ---
    private val localeUS = Locale.US // Example: $1,234.56
    private val currencyUS = Currency.getInstance(localeUS)
    private val decimalDigitsUS = currencyUS.defaultFractionDigits // Should be 2

    private val localeMY = Locale("ms", "MY") // Example: RM1,234.56
    private val currencyMY = Currency.getInstance(localeMY)
    private val decimalDigitsMY = currencyMY.defaultFractionDigits // Should be 2

    private val localeJP = Locale.JAPAN // Example: ¥1,234 (no decimals)
    private val currencyJP = Currency.getInstance(localeJP)
    private val decimalDigitsJP = currencyJP.defaultFractionDigits // Should be 0

    // --- Tests for filter().text (Formatted Output) ---
    @Test
    fun `filter - empty input - US locale - shows zero amount with currency symbol`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeUS)
        val result = transformation.filter(AnnotatedString(""))
        assertThat(result.text.text).isEqualTo("$0.00")
    }

    @Test
    fun `filter - empty input - MY locale - shows zero amount with currency symbol`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeMY)
        val result = transformation.filter(AnnotatedString(""))
        assertThat(result.text.text).isEqualTo("RM0.00") // Assuming your impl prefixes RM
    }

    @Test
    fun `filter - empty input - JP locale - shows zero amount with currency symbol`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeJP)
        val result = transformation.filter(AnnotatedString(""))
        // Note: NumberFormat for JPY usually prefixes with "￥" (full-width) or "¥" (half-width)
        // Check what your Currency.getInstance(localeJP).symbol or NumberFormat outputs
        // For this test, let's assume it's the standard "¥"
        assertThat(result.text.text).isEqualTo("￥0")
    }


    @Test
    fun `filter - single digit input - US locale`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeUS, fixedDecimalDigits = 2)
        val result = transformation.filter(AnnotatedString("1")) // Represents $0.01
        assertThat(result.text.text).isEqualTo("$0.01")
    }

    @Test
    fun `filter - two digits input - US locale`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeUS, fixedDecimalDigits = 2)
        val result = transformation.filter(AnnotatedString("12")) // Represents $0.12
        assertThat(result.text.text).isEqualTo("$0.12")
    }

    @Test
    fun `filter - three digits input - US locale`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeUS, fixedDecimalDigits = 2)
        val result = transformation.filter(AnnotatedString("123")) // Represents $1.23
        assertThat(result.text.text).isEqualTo("$1.23")
    }


    @Test
    fun `filter - multiple digits input with grouping - US locale`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeUS, fixedDecimalDigits = 2)
        val result = transformation.filter(AnnotatedString("1234567")) // Represents $12,345.67
        assertThat(result.text.text).isEqualTo("$12,345.67")
    }

    @Test
    fun `filter - input for zero minor units - US locale`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeUS, fixedDecimalDigits = 2)
        val result = transformation.filter(AnnotatedString("50000")) // Represents $500.00
        assertThat(result.text.text).isEqualTo("$500.00")
    }

    @Test
    fun `filter - multiple digits input - MY locale`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeMY, fixedDecimalDigits = 2)
        val result = transformation.filter(AnnotatedString("1234567")) // Represents RM12,345.67
        // Note: Malaysian Ringgit formatting might vary slightly by Java version/locale data provider.
        // Adjust if your environment produces "RM 12,345.67" (with space) or other variations.
        // The key is consistency with how your formatToCurrency method works.
        assertThat(result.text.text).isEqualTo("RM12,345.67")
    }

    @Test
    fun `filter - multiple digits input - JP locale (0 decimal digits)`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeJP, fixedDecimalDigits = 0)
        val result = transformation.filter(AnnotatedString("12345")) // Represents ¥12,345
        assertThat(result.text.text).isEqualTo("￥12,345")
    }

    @Test
    fun `filter - input containing non-digits - should filter them out`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeUS, fixedDecimalDigits = 2)
        val result = transformation.filter(AnnotatedString("1a2b3c4.5,6")) // Should process as "123456"
        assertThat(result.text.text).isEqualTo("$1,234.56")
    }

    @Test
    fun `filter - large number - US locale`() {
        val transformation = CurrencyAmountInputVisualTransformation(locale = localeUS, fixedDecimalDigits = 2)
        val result = transformation.filter(AnnotatedString("123456789012")) // $1,234,567,890.12
        assertThat(result.text.text).isEqualTo("$1,234,567,890.12")
    }

    @Test
    fun `forceCursorToEndOffsetMapping - originalToTransformed - always returns end of transformed`() {
        val mapping = ForceCursorToEndOffsetMapping(transformedTextLength = 10, originalTextLength = 5)
        assertThat(mapping.originalToTransformed(0)).isEqualTo(10)
        assertThat(mapping.originalToTransformed(3)).isEqualTo(10)
        assertThat(mapping.originalToTransformed(5)).isEqualTo(10)
    }

    @Test
    fun `forceCursorToEndOffsetMapping - transformedToOriginal - always returns end of original`() {
        val mapping = ForceCursorToEndOffsetMapping(transformedTextLength = 10, originalTextLength = 5)
        assertThat(mapping.transformedToOriginal(0)).isEqualTo(5)
        assertThat(mapping.transformedToOriginal(5)).isEqualTo(5)
        assertThat(mapping.transformedToOriginal(10)).isEqualTo(5)
    }

    @Test
    fun `filter with ForceCursorToEndOffsetMapping - cursor behavior`() {
        val transformation = CurrencyAmountInputVisualTransformation(
            locale = localeUS,
            fixedDecimalDigits = 2
        )

        val original = "12345" // $123.45
        val result = transformation.filter(AnnotatedString(original))
        val formattedLength = result.text.length // e.g., "$123.45".length which is 7

        // Check originalToTransformed
        assertThat(result.offsetMapping.originalToTransformed(0)).isEqualTo(formattedLength)
        assertThat(result.offsetMapping.originalToTransformed(original.length / 2)).isEqualTo(formattedLength)
        assertThat(result.offsetMapping.originalToTransformed(original.length)).isEqualTo(formattedLength)

        // Check transformedToOriginal
        assertThat(result.offsetMapping.transformedToOriginal(0)).isEqualTo(original.length)
        assertThat(result.offsetMapping.transformedToOriginal(formattedLength / 2)).isEqualTo(original.length)
        assertThat(result.offsetMapping.transformedToOriginal(formattedLength)).isEqualTo(original.length)
    }
}