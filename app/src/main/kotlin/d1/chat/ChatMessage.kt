package d1.chat

/**
 * Простое представление сообщения чата.
 *
 * Используем только данные, без наследования и сложных иерархий.
 */
data class ChatMessage(
    val role: String,   // "user" или "assistant"
    val content: String // текст сообщения
)


