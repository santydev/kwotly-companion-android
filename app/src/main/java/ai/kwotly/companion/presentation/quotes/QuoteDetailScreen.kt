package ai.kwotly.companion.presentation.quotes

import ai.kwotly.companion.domain.model.QuoteDetail
import ai.kwotly.companion.domain.model.QuoteItem
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant

@Composable
fun QuoteDetailRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuoteDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    QuoteDetailScreen(
        state = state,
        onBack = onBack,
        onRetry = viewModel::load,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteDetailScreen(
    state: QuoteDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    val title = (state as? QuoteDetailUiState.Success)?.quote?.title ?: "Quote"
                    Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        when (state) {
            QuoteDetailUiState.Loading -> Centered(padding) { CircularProgressIndicator() }

            is QuoteDetailUiState.Error -> Centered(padding) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) {
                        Text("Retry")
                    }
                }
            }

            is QuoteDetailUiState.Success -> QuoteContent(state.quote, padding)
        }
    }
}

@Composable
private fun QuoteContent(quote: QuoteDetail, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = padding.calculateTopPadding() + 16.dp,
            bottom = 24.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column {
                Text(quote.clientName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${quote.status.name.lowercase().replaceFirstChar { it.uppercase() }} · ${quote.createdAt.formatDate()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp),
                )
                if (quote.description.isNotBlank()) {
                    Text(
                        text = quote.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        }

        if (quote.items.isNotEmpty()) {
            item { SectionLabel("Line items") }
            items(quote.items, key = { it.id }) { item -> ItemRow(item, quote.currency) }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            TotalsBlock(quote)
        }
    }
}

@Composable
private fun ItemRow(item: QuoteItem, currency: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.description, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${trimNumber(item.quantity)} ${item.unit} × ${formatMoney(item.unitPrice, currency)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = formatMoney(item.total, currency),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Composable
private fun TotalsBlock(quote: QuoteDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        AmountRow("Subtotal", formatMoney(quote.subtotal, quote.currency))
        AmountRow("Tax", formatMoney(quote.taxAmount, quote.currency))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            // Money moment — legendary gold.
            Text(
                text = formatMoney(quote.total, quote.currency),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        if (quote.depositAmount > 0.0) {
            AmountRow(
                label = if (quote.depositPaid) "Deposit (paid)" else "Deposit due",
                value = formatMoney(quote.depositAmount, quote.currency),
                valueColor = if (quote.depositPaid) MaterialTheme.colorScheme.tertiary else null,
            )
        }
    }
}

@Composable
private fun AmountRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor ?: MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun Centered(padding: PaddingValues, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

/** Drops a trailing ".0" so whole quantities read "2" not "2.0". */
private fun trimNumber(value: Double): String =
    if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0B)
@Composable
private fun QuoteDetailPreview() {
    KwotlyCompanionTheme {
        QuoteDetailScreen(
            state = QuoteDetailUiState.Success(
                QuoteDetail(
                    id = "1",
                    title = "Kitchen remodel",
                    clientName = "Jane Doe",
                    description = "Full gut + new cabinetry and counters.",
                    status = QuoteStatus.ACCEPTED,
                    subtotal = 11500.0,
                    taxAmount = 1000.0,
                    total = 12500.0,
                    currency = "USD",
                    notes = null,
                    depositPaid = true,
                    depositAmount = 3750.0,
                    createdAt = Instant.now(),
                    items = listOf(
                        QuoteItem("a", "Cabinets", 1.0, "set", 6000.0, 6000.0),
                        QuoteItem("b", "Quartz counter", 32.0, "sqft", 95.0, 3040.0),
                    ),
                ),
            ),
            onBack = {},
            onRetry = {},
        )
    }
}
