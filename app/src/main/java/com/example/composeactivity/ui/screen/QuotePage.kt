package com.example.composeactivity.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeactivity.data.Quote
import com.example.composeactivity.viewmodel.QuoteViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeactivity.data.QuoteApi
import com.example.composeactivity.data.QuoteDao
import com.example.composeactivity.data.QuoteEntity
import com.example.composeactivity.data.QuoteRepository
import com.example.composeactivity.data.QuotesResponse
import com.example.composeactivity.ui.theme.AppSpacing
import com.example.composeactivity.ui.theme.IconDefaultColor
import com.example.composeactivity.ui.theme.LikedColor
import com.example.composeactivity.ui.theme.SavedColor
import com.example.composeactivity.ui.theme.quoteBackground
import kotlinx.coroutines.flow.Flow
import com.example.composeactivity.viewmodel.SettingsViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun QuoteScreen(
    viewModel: QuoteViewModel,
    settingsViewModel: SettingsViewModel
) {
    Column {
        QuotePage(viewModel, settingsViewModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuotePage(quoteViewModel: QuoteViewModel = viewModel(), settingsViewModel: SettingsViewModel) {
    val preferenceCategories by settingsViewModel.selectedCategories.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All") + preferenceCategories.toList().sorted()

    val quotes by quoteViewModel.quotes.collectAsState()

    val filteredQuotes by remember(quotes, preferenceCategories, selectedCategory) {
        derivedStateOf {
            val preferenceFiltered = quotes.filter { it.category in preferenceCategories }

            if (selectedCategory == "All") preferenceFiltered
            else preferenceFiltered.filter { it.category == selectedCategory }
        }
    }


    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { filteredQuotes.size }
    )

    LaunchedEffect(selectedCategory) {
        pagerState.scrollToPage(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            )
            .padding(8.dp)
    ) {
        CategoryTabs(categories, selectedCategory) { selectedCategory = it }
        if (filteredQuotes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val message = if (preferenceCategories.isEmpty()) {
                    "Please select categories in settings to see quotes."
                } else {
                    "No Quotes Available for the selected categories."
                }
                Text(text = message, color = Color.White, fontSize = 18.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                QuoteCard(filteredQuotes[page], quoteViewModel)
            }
        }
    }
}

@Composable
fun QuoteCard(quote: Quote, quoteViewModel: QuoteViewModel) {

    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val spacing = AppSpacing
    val shape = MaterialTheme.shapes

    Card(
        shape = shape.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.small),
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.quoteBackground)
                .padding(spacing.large),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = quote.quote,
                color = colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
                style = typography.titleLarge,
                maxLines = 10
            )
            Spacer(modifier = Modifier.height(spacing.medium))
            Text(
                text = quote.author,
                style = typography.bodyMedium,
                color = colorScheme.onSurface,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(spacing.large))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { quoteViewModel.toggleLike(quote) }) {
                    Icon(
                        imageVector = if (quote.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (quote.isLiked) LikedColor else IconDefaultColor,

                    )
                }

                IconButton(onClick = { quoteViewModel.toggleSave(quote) }) {
                    Icon(
                        imageVector = if (quote.isSaved) Icons.Outlined.AddCircle else Icons.Filled.Add,
                        contentDescription = "Save",
                        tint = if (quote.isSaved) SavedColor else IconDefaultColor,

                    )
                }

                IconButton(onClick = { shareQuote(context, quote) }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = IconDefaultColor,
                    )
                }
            }
        }
    }
}

fun shareQuote(context: Context, quote: Quote) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "\"${quote.quote}\" - ${quote.author}")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

@Composable
fun QuoteSectionPreview() {
    val mockQuotes = listOf(
        Quote(
            1,
            "Your heart is the size of an ocean. Go find yourself in its hidden depths.",
            "Rumi",
            "Love"
        ),
        Quote(
            2,
            "Thinking is the capital, Enterprise is the way, Hard Work is the solution.",
            "Abdul Kalam",
            "ABC"
        ),
        Quote(3, "The best way to predict the future is to invent it.", "Alan Kay", "Nill")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        QuoteSectionPreviewContent(mockQuotes)
    }
}

@Composable
fun QuoteSectionPreviewContent(
    quotes: List<Quote>,
    viewModel: QuoteViewModel = QuoteViewModel(QuoteRepository(MockQuoteDao(), MockApi()))
) {
    Column {
        quotes.forEach { quote ->
            QuoteCard(quote, viewModel)
        }
    }
}

class MockQuoteDao : QuoteDao {
    override fun getAllQuotes() = flowOf(emptyList<QuoteEntity>())
    override fun getAllLikedQuotes(): Flow<List<QuoteEntity>> {
        TODO("Not yet implemented")
    }

    override fun getAllSavedQuotes(): Flow<List<QuoteEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertQuotes(quotes: List<QuoteEntity>) {}
    override suspend fun updateLikeStatus(id: Int, isLiked: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSaveStatus(id: Int, isSaved: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllQuotes() {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun searchQuotes(query: String): List<Quote> {
        TODO("Not yet implemented")
    }

}

class MockApi : QuoteApi {
    override suspend fun getQuotes(): QuotesResponse {
        TODO("Not yet implemented")
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewQuoteSection() {
    QuoteSectionPreview()
}

