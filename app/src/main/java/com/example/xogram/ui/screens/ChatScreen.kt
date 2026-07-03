package com.example.xogram.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xogram.data.model.Message

// Цветовая палитра в стиле Telegram
val TelegramDarkBlue = Color(0xFF17212B)
val TelegramLightBlue = Color(0xFF24303F)
val TelegramOutgoingBubble = Color(0xFF2B5278)
val TelegramIncomingBubble = Color(0xFF182533)
val TelegramText = Color(0xFFF5F5F5)
val TelegramSubText = Color(0xFF7F91A4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    currentUserId: String,
    messages: List<Message>,
    onSendMessage: (String) -> Unit
) {
    var textState by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("xoGram Чат", color = TelegramText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("в сети", color = Color(0xFF5288C1), fontSize = 13.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Назад */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = TelegramText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TelegramDarkBlue)
            )
        },
        containerColor = TelegramLightBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Список сообщений
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                reverseLayout = true
            ) {
                items(messages) { message ->
                    val isOwnMessage = message.senderId == currentUserId
                    MessageBubble(message = message, isOwnMessage = isOwnMessage)
                }
            }

            // Панель ввода в стиле TG
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TelegramDarkBlue)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = textState,
                    onValueChange = { textState = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Сообщение...", color = TelegramSubText) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = TelegramText,
                        unfocusedTextColor = TelegramText,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                
                if (textState.isNotBlank()) {
                    IconButton(
                        onClick = {
                            onSendMessage(textState)
                            textState = ""
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF5288C1))
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Отправить", tint = TelegramText)
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isOwnMessage: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        contentAlignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = if (isOwnMessage) TelegramOutgoingBubble else TelegramIncomingBubble,
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (isOwnMessage) 12.dp else 0.dp,
                        bottomEnd = if (isOwnMessage) 0.dp else 12.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .widthIn(max = 280.dp)
        ) {
            if (!isOwnMessage) {
                Text(
                    text = message.senderName,
                    color = Color(0xFF5288C1),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Text(
                text = message.text,
                color = TelegramText,
                fontSize = 16.sp
            )
        }
    }
}
