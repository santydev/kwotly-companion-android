package ai.kwotly.companion.domain.model

import java.time.Instant

/** List-row summary of a quote. Full line items live in [QuoteDetail]. */
data class Quote(
    val id: String,
    val title: String,
    val clientName: String,
    val status: QuoteStatus,
    val total: Double,
    val currency: String,
    val createdAt: Instant?,
    val depositPaid: Boolean,
)

/**
 * Quote lifecycle. Mirrors the backend QuoteStatus enum; [UNKNOWN] absorbs
 * any future status the app hasn't shipped support for yet.
 */
enum class QuoteStatus {
    DRAFT,
    SENT,
    VIEWED,
    ACCEPTED,
    DECLINED,
    EXPIRED,
    CANCELLED,
    VOIDED,
    COMPLETED,
    UNKNOWN;

    companion object {
        fun fromApi(raw: String): QuoteStatus =
            entries.firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: UNKNOWN
    }
}