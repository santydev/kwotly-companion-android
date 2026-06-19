package ai.kwotly.companion.presentation.quotes

import ai.kwotly.companion.domain.model.Quote

/** Exhaustive state for the quotes list. Empty is just Success(emptyList). */
sealed interface QuotesListUiState {
    data object Loading : QuotesListUiState
    data class Success(val quotes: List<Quote>) : QuotesListUiState
    data class Error(val message: String) : QuotesListUiState
}
