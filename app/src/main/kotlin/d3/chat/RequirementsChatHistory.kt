package d3.chat

/**
 * Одна реплика диалога: роль + текст.
 * Роли: "user" или "assistant".
 */
data class RequirementsMessage(
    val role: String,
    val content: String
)

/**
 * История диалога в d3.
 */
class RequirementsChatHistory {

    private val messages: MutableList<RequirementsMessage> = mutableListOf()

    fun addUserMessage(text: String) {
        messages.add(RequirementsMessage("user", text))
    }

    fun addAssistantMessage(text: String) {
        messages.add(RequirementsMessage("assistant", text))
    }

    fun all(): List<RequirementsMessage> = messages.toList()

    fun size(): Int = messages.size
}

