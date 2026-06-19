package ai.kwotly.companion.data.remote.dto

import ai.kwotly.companion.domain.model.Quote
import ai.kwotly.companion.domain.model.QuoteDetail
import ai.kwotly.companion.domain.model.QuoteItem
import ai.kwotly.companion.domain.model.QuoteStatus
import kotlinx.serialization.Serializable
import java.time.Instant

// ---- GET /api/quotes/list -> { quotes: [...] } ----

@Serializable
data class QuotesListResponse(
    val quotes: List<QuoteSummaryDto>,
)

@Serializable
data class QuoteSummaryDto(
    val id: String,
    val title: String,
    val clientName: String,
    val status: String,
    val total: Double = 0.0,
    val currency: String = "USD",
    val createdAt: String? = null,
    val depositPaid: Boolean = false,
)

// ---- GET /api/quotes/[id] -> { quote: {...} } ----

@Serializable
data class QuoteDetailResponse(
    val quote: QuoteDetailDto,
)

@Serializable
data class QuoteDetailDto(
    val id: String,
    val title: String,
    val clientName: String,
    val description: String = "",
    val status: String,
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val total: Double = 0.0,
    val currency: String = "USD",
    val notes: String? = null,
    val depositPaid: Boolean = false,
    val depositAmount: Double = 0.0,
    val createdAt: String? = null,
    val items: List<QuoteItemDto> = emptyList(),
)

@Serializable
data class QuoteItemDto(
    val id: String,
    val description: String,
    val quantity: Double = 1.0,
    val unit: String = "ea",
    val unitPrice: Double = 0.0,
    val total: Double = 0.0,
)

// ---- mappers ----

private fun String?.toInstantOrNull(): Instant? =
    this?.let { runCatching { Instant.parse(it) }.getOrNull() }

fun QuoteSummaryDto.toDomain(): Quote = Quote(
    id = id,
    title = title,
    clientName = clientName,
    status = QuoteStatus.fromApi(status),
    total = total,
    currency = currency,
    createdAt = createdAt.toInstantOrNull(),
    depositPaid = depositPaid,
)

fun QuoteDetailDto.toDomain(): QuoteDetail = QuoteDetail(
    id = id,
    title = title,
    clientName = clientName,
    description = description,
    status = QuoteStatus.fromApi(status),
    subtotal = subtotal,
    taxAmount = taxAmount,
    total = total,
    currency = currency,
    notes = notes,
    depositPaid = depositPaid,
    depositAmount = depositAmount,
    createdAt = createdAt.toInstantOrNull(),
    items = items.map { it.toDomain() },
)

fun QuoteItemDto.toDomain(): QuoteItem = QuoteItem(
    id = id,
    description = description,
    quantity = quantity,
    unit = unit,
    unitPrice = unitPrice,
    total = total,
)