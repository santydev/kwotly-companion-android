package ai.kwotly.companion.presentation.quotes

import ai.kwotly.companion.data.repository.QuotesRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteDetailViewModel @Inject constructor(
    private val quotesRepository: QuotesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Provided by the nav route "quotes/{quoteId}".
    private val quoteId: String = checkNotNull(savedStateHandle["quoteId"]) {
        "QuoteDetailViewModel requires a quoteId nav argument"
    }

    private val _uiState = MutableStateFlow<QuoteDetailUiState>(QuoteDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = QuoteDetailUiState.Loading
            quotesRepository.getQuote(quoteId)
                .onSuccess { quote -> _uiState.value = QuoteDetailUiState.Success(quote) }
                .onFailure { cause ->
                    _uiState.value = QuoteDetailUiState.Error(cause.message ?: "Couldn't load this quote.")
                }
        }
    }
}
