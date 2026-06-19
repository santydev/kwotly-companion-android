package ai.kwotly.companion.presentation.quotes

import ai.kwotly.companion.domain.model.QuoteDetail

sealed interface QuoteDetailUiState {
    data object Loading : QuoteDetailUiState
    data class Success(val quote: QuoteDetail) : QuoteDetailUiState
    data class Error(val message: String) : QuoteDetailUiState
}
