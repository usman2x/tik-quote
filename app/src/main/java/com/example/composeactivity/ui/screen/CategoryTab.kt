package com.example.composeactivity.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(
            text = category,
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.Serif
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryTabs() {
    val categories = listOf("Motivation", "Success", "Happiness", "Life", "Wisdom")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    CategoryTabs(categories, selectedCategory) { selectedCategory = it }
}