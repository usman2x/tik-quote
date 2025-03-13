package com.example.composeactivity.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeactivity.data.Article


@Composable
fun ArticleSection(articles: List<Article>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(text = "Published Articles", fontSize = 22.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.height(300.dp) // Set scrollable height
        ) {
            items(articles) { article ->
                ArticleItem(article)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ArticleItem(article: Article) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(text = article.title, fontSize = 18.sp, color = Color.Black)
        ClickableLink(url = article.link)
        Text(text = article.date, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun ClickableLink(url: String) {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
        append(url)
        pop()
    }

    Text(
        text = annotatedString,
        fontSize = 16.sp,
        modifier = Modifier.clickable {
            openUrl(context, url)
        }
    )
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}