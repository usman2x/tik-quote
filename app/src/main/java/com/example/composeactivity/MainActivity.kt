package com.example.composeactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.composeactivity.data.QuoteDatabase
import com.example.composeactivity.data.QuoteRepository
import com.example.composeactivity.data.RetrofitInstance
import com.example.composeactivity.ui.screen.QuoteScreen
import com.example.composeactivity.viewmodel.QuoteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = QuoteDatabase.getDatabase(this)
        val repository = QuoteRepository(database.quoteDao(), RetrofitInstance.api)
        val viewModel = QuoteViewModel(repository)

        setContent {
            QuoteScreen(viewModel)
        }
    }
}