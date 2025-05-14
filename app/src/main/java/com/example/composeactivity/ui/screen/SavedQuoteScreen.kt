package com.example.composeactivity.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeactivity.viewmodel.QuoteViewModel
@Composable
fun SavedQuoteScreen(viewModel: QuoteViewModel) {
    Column {
        SavedQuotePage(viewModel)
    }
}

@Composable
fun SavedQuotePage(quoteViewModel: QuoteViewModel = viewModel()) {

    val quotes by quoteViewModel.savedQuotes.collectAsState()


    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { quotes.size }
    )

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
        if (quotes.isEmpty()) {
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
                QuoteCard(quotes[page], quoteViewModel)
            }
        }
    }
}