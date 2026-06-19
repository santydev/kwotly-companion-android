package ai.kwotly.companion.presentation.quotes

import ai.kwotly.companion.domain.model.Quote
import ai.kwotly.companion.domain.model.QuoteStatus
import ai.kwotly.companion.presentation.util.formatDate
import ai.kwotly.companion.presentation.util.formatMoney
import ai.kwotly.companion.theme.KwotlyCompanionTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant

@Composable
fun QuotesListRoute(
    onQuoteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuotesListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    QuotesListScreen(
        state = state,
        onQuoteClick = onQuoteClick,
        onRetry = viewModel::load,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuotesListScreen(
    state: QuotesListUiState,
    onQuoteClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Quotes") }) },
    ) { padding ->
        when (state) {
            QuotesListUiState.Loading -> CenteredBox(padding) {
                CircularProgressIndicator()
            }

            is QuotesListUiState.Error -> CenteredBox(padding) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) {
                        Text("Retry")
                    }
                }
            }

            is QuotesListUiState.Success ->
                if (state.quotes.isEmpty()) {
                    CenteredBox(padding) {
                        Text(
                            text = "No quotes yet.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(state.quotes, key = { it.id }) { quote ->
                            QuoteCard(quote = quote, onClick = { onQuoteClick(quote.id) })
                        }
                    }
                }
        }
    }
}

@Composable
private fun CenteredBox(
    padding: PaddingValues,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

@Composable
private fun QuoteCard(quote: Quote, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = quote.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                StatusBadge(quote.status)
            }
            Text(
                text = quote.clientName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Money moment — legendary gold.
                Text(
                    text = formatMoney(quote.total, quote.currency),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                Text(
                    text = quote.createdAt.formatDate(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: QuoteStatus) {
    val color = status.tierColor()
    Surface(
        color = color.copy(alpha = 0.18f),
        contentColor = color,
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = status.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

/** Maps lifecycle status to a WoW tier color. */
@Composable
private fun QuoteStatus.tierColor(): Color = when (this) {
    QuoteStatus.ACCEPTED, QuoteStatus.COMPLETED -> MaterialTheme.colorScheme.tertiary // gold
    QuoteStatus.SENT, QuoteStatus.VIEWED -> MaterialTheme.colorScheme.primary // rare blue
    QuoteStatus.DRAFT -> MaterialTheme.colorScheme.onSurfaceVariant // zinc
    else -> MaterialTheme.colorScheme.error // declined/expired/cancelled/voided
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0B)
@Composable
private fun QuotesListPreview() {
    KwotlyCompanionTheme {
        QuotesListScreen(
            state = QuotesListUiState.Success(
                listOf(
                    Quote("1", "Kitchen remodel", "Jane Doe", QuoteStatus.ACCEPTED, 12500.0, "USD", Instant.now(), true),
                    Quote("2", "Bathroom tiling", "John Smith", QuoteStatus.SENT, 4200.0, "USD", Instant.now(), false),
                    Quote("3", "Deck rebuild", "Acme LLC", QuoteStatus.DRAFT, 8800.0, "USD", Instant.now(), false),
                ),
            ),
            onQuoteClick = {},
            onRetry = {},
        )
    }
}
