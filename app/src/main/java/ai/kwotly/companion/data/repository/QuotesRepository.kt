package ai.kwotly.companion.data.repository

import ai.kwotly.companion.domain.model.Quote
import ai.kwotly.companion.domain.model.QuoteDetail

/** Reads the authenticated contractor's quotes from the backend. */
interface QuotesRepository {

    suspend fun getQuotes(): Result<List<Quote>>

    suspend fun getQuote(id: String): Result<QuoteDetail>
}
