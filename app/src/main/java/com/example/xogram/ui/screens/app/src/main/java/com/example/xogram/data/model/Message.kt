package com.example.xogram.data.model

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
