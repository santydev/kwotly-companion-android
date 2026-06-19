package ai.kwotly.companion.presentation.util

import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale

private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US)

/** Formats an amount as currency, e.g. 1234.5 + "USD" -> "$1,234.50". */
fun formatMoney(amount: Double, currencyCode: String): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    runCatching { format.currency = Currency.getInstance(currencyCode) }
    return format.format(amount)
}

/** Formats an instant in the device timezone, e.g. "Jun 18, 2026". Empty when null. */
fun Instant?.formatDate(): String =
    this?.atZone(ZoneId.systemDefault())?.format(dateFormatter).orEmpty()
