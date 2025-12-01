package d1.chat

/**
 * Builder для DSL над историей.
 */
class ConversationBuilder(
    private val history: ChatHistory
) {
    fun user(text: String) {
        history.addUserMessage(text)
    }

    fun assistant(text: String) {
        history.addAssistantMessage(text)
    }
}


