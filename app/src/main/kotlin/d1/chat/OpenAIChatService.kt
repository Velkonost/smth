package d1.chat

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.clients.openai.OpenAIModels

/**
 * Сервис, который общается с OpenAI через Koog и обновляет историю.
 */
class OpenAIChatService(
    private val client: OpenAILLMClient,
    private val history: ChatHistory,
    private val systemPrompt: String = "Ты помощник в терминальном чате. Отвечай кратко и по делу. Если пользователь просит код, отвечай на русском языке."
) {

    /**
     * Отправляет новое сообщение пользователя, дергает OpenAI и возвращает ответ ассистента.
     */
    suspend fun sendUserMessage(userInput: String): String {
        // Добавляем в историю через простой DSL
        history.conversation {
            user(userInput)
        }

        // На основе всей истории строим prompt Koog DSL-ом.
        val builtPrompt = prompt("terminal_chat") {
            system(systemPrompt)

            for (message in history.all()) {
                when (message.role) {
                    "user" -> user(message.content)
                    "assistant" -> assistant(message.content)
                }
            }
        }

        val response = client.execute(
            prompt = builtPrompt,
            model = OpenAIModels.Chat.GPT4o
        )

        val replyText = response.firstOrNull()?.content ?: "<empty response>"

        // Сохраняем ответ ассистента в историю
        history.conversation {
            assistant(replyText)
        }

        return replyText
    }
}


