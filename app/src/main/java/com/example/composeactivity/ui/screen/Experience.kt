package com.example.composeactivity.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeactivity.data.Experience

@Composable
fun ExperienceSection(experiences: List<Experience>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(text = "Experience", fontSize = 22.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))

        experiences.forEach { experience ->
            ExperienceItem(experience)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ExperienceItem(experience: Experience) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(text = experience.title, fontSize = 18.sp, color = Color.Black)
        Text(text = experience.duration, fontSize = 16.sp, color = Color.DarkGray)
    }
}