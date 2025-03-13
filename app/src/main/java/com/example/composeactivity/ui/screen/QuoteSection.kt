package com.example.composeactivity.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

val categories = listOf("All", "Motivational", "Emotional")

@Composable
fun QuoteScreen() {
    val quoteViewModel: QuoteViewModel = viewModel()

    Column {
        QuotePage(quoteViewModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuotePage(quoteViewModel: QuoteViewModel = viewModel()) {

    var selectedCategory by remember { mutableStateOf(categories.first()) }

    val filteredQuotes by remember {
        derivedStateOf {
            if (selectedCategory == "All") quoteViewModel.quotes
            else quoteViewModel.quotes.filter { it.category == selectedCategory }
        }
    }

    val pagerState = rememberPagerState(pageCount = { filteredQuotes.size })

    Scaffold(
        topBar = { AppBar(title = "Quotes") },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color(0xFF1A1A2E), Color(0xFF16213E))))
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                CategoryTabs(categories, selectedCategory) { selectedCategory = it }
                Spacer(modifier = Modifier.height(8.dp))
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { page ->
                    QuoteCard(quoteViewModel.quotes[page])
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF16213E), // Dark background
            titleContentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryTab(category, category == selectedCategory) { onCategorySelected(category) }
        }
    }
}

@Composable
fun CategoryTab(category: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor =
        if (isSelected) Color(0xFFFFC300) else Color(0xFFEAEAEA).copy(alpha = 0.7f)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = category, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun QuoteCard(quote: Quote) {
    var isLiked by remember { mutableStateOf(quote.isLiked) }
    var isSaved by remember { mutableStateOf(quote.isSaved) }
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
                IconButton(onClick = { isLiked = !isLiked }) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.Black
                    )
                }

                IconButton(onClick = { isSaved = !isSaved }) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Add else Icons.Outlined.AddCircle,
                        contentDescription = "Save",
                        tint = if (isSaved) Color.Blue else Color.Black
                    )
                }

                IconButton(onClick = { shareQuote(context, quote) }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

// Share Function
fun shareQuote(context: Context, quote: Quote) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "\"${quote.quote}\" - ${quote.author}")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

@Composable
fun QuoteSection(quoteViewModel: QuoteViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Famous Quotes", fontSize = 22.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(quoteViewModel.quotes) { quote ->
                QuoteItem(quote)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun QuoteItem(quote: Quote) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray, shape = RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Text(text = "\"${quote.quote}\"", fontSize = 18.sp, fontStyle = FontStyle.Italic)
        Text(text = "- ${quote.author}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
fun QuoteSectionPreviewContent(quotes: List<Quote>) {
    Column {
        quotes.forEach { quote ->
            QuoteCard(quote)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuoteSection() {
    QuoteSectionPreview()
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryTabs() {
    val categories = listOf("Motivation", "Success", "Happiness", "Life", "Wisdom")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    CategoryTabs(categories, selectedCategory) { selectedCategory = it }
}
