package com.maltsev.stankinhack.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maltsev.stankinhack.screens.postsList
import com.maltsev.stankinhack.utils.model.Post

@Composable
fun BlogList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(postsList) { post ->
            PostPreview(post = post)
        }
    }
}

@Preview
@Composable
fun PPostPreview() {
    PostPreview(post = Post(1, "Проверка дизайна", "Сделаем проверку дизайна, чтобы понять, что да как", "07/06/2022"))
}

@Composable
fun PostPreview(post: Post) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .clip(RoundedCornerShape(10))
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = post.label,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .padding(vertical = 5.dp)
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = post.date,
                fontWeight = FontWeight.Thin,
                color = Color.DarkGray,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Right
            )
            Text(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 7.dp),
                text = post.text,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Justify
            )

            Divider(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 5.dp, start = 3.dp, end = 3.dp),
                color = MaterialTheme.colors.primary
            )
        }

    }
}