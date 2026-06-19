package ai.kwotly.companion.domain.model

import java.time.Instant

/** Full quote with line items, for the detail screen. */
data class QuoteDetail(
    val id: String,
    val title: String,
    val clientName: String,
    val description: String,
    val status: QuoteStatus,
    val subtotal: Double,
    val taxAmount: Double,
    val total: Double,
    val currency: String,
    val notes: String?,
    val depositPaid: Boolean,
    val depositAmount: Double,
    val createdAt: Instant?,
    val items: List<QuoteItem>,
)

data class QuoteItem(
    val id: String,
    val description: String,
    val quantity: Double,
    val unit: String,
    val unitPrice: Double,
    val total: Double,
)