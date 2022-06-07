package com.maltsev.stankinhack.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maltsev.stankinhack.BOT_SENDER
import com.maltsev.stankinhack.screens.messagesList
import com.maltsev.stankinhack.utils.model.Message

@Composable
fun ChatUI() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        items(messagesList) { message ->
            MessageRow(message = message)
        }
    }
}

@Composable
fun MessageRow(message: Message) {
    Row (
            horizontalArrangement = if (message.sender == BOT_SENDER) Arrangement.Start else Arrangement.End,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
            ) {
        Box (
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(30))
                .background(color = if (message.sender == BOT_SENDER) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.secondaryVariant)
                .border(
                    color = if (message.sender == BOT_SENDER) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                    width = 2.dp,
                    shape = RoundedCornerShape(30)
                )
                ) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .defaultMinSize(minHeight = 25.dp),
                text = message.text,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
                )
        }

    }
}

fun <T> SnapshotStateList<T>.swapList(newList: SnapshotStateList<T>) {
    clear()
    addAll(newList)
}
