package com.example.composeactivity.ui.screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeactivity.data.Quote
import com.example.composeactivity.viewmodel.QuoteViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun QuoteScreen(viewModel: QuoteViewModel) {
    Column {
        QuotePage(viewModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuotePage(quoteViewModel: QuoteViewModel = viewModel()) {
    val context = LocalContext.current

    var selectedCategory by remember { mutableStateOf("All") }
    val categories = quoteViewModel.categories

    val quotes by quoteViewModel.quotes.collectAsState()

    val filteredQuotes by remember(quotes, selectedCategory) {
        derivedStateOf {
            if (selectedCategory == "All") quotes
            else quotes.filter { it.category == selectedCategory }
        }
    }


    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { filteredQuotes.size }
    )

    LaunchedEffect(selectedCategory) {
        pagerState.scrollToPage(0)
    }

    Scaffold(
        topBar = {
            AppBar(title = "Quotes") {
                quoteViewModel.syncQuotes {
                    Toast.makeText(context, "Sync Completed!", Toast.LENGTH_SHORT).show()
                }
            }
        },
        content = { paddingValues ->
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
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                CategoryTabs(categories, selectedCategory) { selectedCategory = it }
                Spacer(modifier = Modifier.height(8.dp))
                if (filteredQuotes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Quotes Available", color = Color.White, fontSize = 18.sp)
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
    )
}

@Composable
fun QuoteCard(quote: Quote, quoteViewModel: QuoteViewModel) {

    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "\"${quote.quote}\"",
                fontSize = 40.sp,
                fontStyle = FontStyle.Italic,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "- ${quote.author}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { quoteViewModel.toggleLike(quote) }) {
                    Icon(
                        imageVector = if (quote.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (quote.isLiked) Color.Red else Color.Black,
                        modifier = Modifier.size(50.dp)
                    )
                }

                IconButton(onClick = { quoteViewModel.toggleSave(quote) }) {
                    Icon(
                        imageVector = if (quote.isSaved) Icons.Outlined.AddCircle else Icons.Filled.Add,
                        contentDescription = "Save",
                        tint = if (quote.isSaved) Color.Blue else Color.Black,
                        modifier = Modifier.size(50.dp)
                    )
                }

                IconButton(onClick = { shareQuote(context, quote) }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = Color.Black,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuoteSectionPreview() {
    val mockQuotes = listOf(
        Quote(
            1,
            "Your heart is the size of an ocean. Go find yourself in its hidden depths.",
            "Rumi",
            ""
        ),
        Quote(
            2,
            "Thinking is the capital, Enterprise is the way, Hard Work is the solution.",
            "Abdul Kalam",
            ""
        ),
        Quote(3, "The best way to predict the future is to invent it.", "Alan Kay", "")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        QuoteSectionPreviewContent(mockQuotes)
    }
}

@Composable
fun QuoteSectionPreviewContent(
    quotes: List<Quote>,
    viewModel: QuoteViewModel = viewModel<QuoteViewModel>()
) {
    Column {
        quotes.forEach { quote ->
            QuoteCard(quote, viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuoteSection() {
    QuoteSectionPreview()
}

fun shareQuote(context: Context, quote: Quote) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "\"${quote.quote}\" - ${quote.author}")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}


