package ai.kwotly.companion.data.repository

import ai.kwotly.companion.data.remote.KwotlyApi
import ai.kwotly.companion.data.remote.toDisplayableError
import ai.kwotly.companion.data.remote.dto.toDomain
import ai.kwotly.companion.domain.model.Quote
import ai.kwotly.companion.domain.model.QuoteDetail
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuotesRepositoryImpl @Inject constructor(
    private val api: KwotlyApi,
    private val json: Json,
) : QuotesRepository {

    override suspend fun getQuotes(): Result<List<Quote>> = runCatching {
        api.getQuotes().quotes.map { it.toDomain() }
    }.recoverCatching { cause -> throw cause.toDisplayableError(json, "Couldn't load quotes") }

    override suspend fun getQuote(id: String): Result<QuoteDetail> = runCatching {
        api.getQuote(id).quote.toDomain()
    }.recoverCatching { cause -> throw cause.toDisplayableError(json, "Couldn't load this quote") }
}
