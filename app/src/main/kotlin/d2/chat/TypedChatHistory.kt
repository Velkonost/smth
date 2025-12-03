package d2.chat

import d2.model.AiAnswer

/**
 * Один шаг диалога для d2: запрос пользователя + типизированный ответ AiAnswer.
 */
data class TypedChatTurn(
    val userInput: String,
    val answer: AiAnswer
)

/**
 * История диалога для d2.
 */
class TypedChatHistory {

    private val turns: MutableList<TypedChatTurn> = mutableListOf()

    fun addTurn(userInput: String, answer: AiAnswer) {
        turns.add(TypedChatTurn(userInput, answer))
    }

    fun all(): List<TypedChatTurn> = turns.toList()

    fun size(): Int = turns.size
}

