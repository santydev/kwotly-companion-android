package ai.kwotly.companion.presentation.quotes

import ai.kwotly.companion.data.repository.QuotesRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesListViewModel @Inject constructor(
    private val quotesRepository: QuotesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuotesListUiState>(QuotesListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = QuotesListUiState.Loading
            quotesRepository.getQuotes()
                .onSuccess { quotes -> _uiState.value = QuotesListUiState.Success(quotes) }
                .onFailure { cause ->
                    _uiState.value = QuotesListUiState.Error(cause.message ?: "Couldn't load quotes.")
                }
        }
    }
}
