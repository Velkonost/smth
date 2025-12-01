package d1.chat

/**
 * История диалога + небольшой DSL для добавления сообщений.
 *
 * Пример:
 * history.conversation {
 *     user("Привет")
 *     assistant("Здравствуйте!")
 * }
 */
class ChatHistory {

    private val messages: MutableList<ChatMessage> = mutableListOf()

    fun addUserMessage(text: String) {
        messages.add(ChatMessage(role = "user", content = text))
    }

    fun addAssistantMessage(text: String) {
        messages.add(ChatMessage(role = "assistant", content = text))
    }

    fun all(): List<ChatMessage> = messages.toList()

    /**
     * DSL-обёртка для удобного добавления сообщений.
     */
    fun conversation(block: ConversationBuilder.() -> Unit) {
        val builder = ConversationBuilder(this)
        builder.block()
    }
}


