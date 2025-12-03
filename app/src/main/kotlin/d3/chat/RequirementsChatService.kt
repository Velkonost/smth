package d3.chat

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.clients.openai.OpenAIModels

/**
 * Сервис, который ведёт диалог для уточнения требований и в конце выдаёт ТЗ.
 */
class RequirementsChatService(
    private val client: OpenAILLMClient,
    private val history: RequirementsChatHistory,
    private val systemPrompt: String = """
        Ты — бизнес-аналитик, который помогает собрать требования для ТЗ на продукт.

        Твоя задача:
        1. Задавать ПО ОДНОМУ понятному уточняющему вопросу за раз, если информации мало.
        2. Постепенно уточнять:
           - цели продукта и ключевые метрики успеха;
           - целевую аудиторию и основные сценарии использования;
           - функциональные и нефункциональные требования;
           - ограничения (сроки, бюджет, платформы, интеграции);
           - риски и открытые вопросы.
        3. Как только информации достаточно, ПРЕКРАТИ задавать вопросы и верни ПОЛНОЦЕННОЕ ТЗ.
           В этом случае начни ответ с маркера:
           [FINAL_TZ]
           а далее выдай структурированный текст ТЗ с разделами:
           - Общее описание
           - Цели
           - Целевая аудитория
           - Основные сценарии
           - Функциональные требования
           - Нефункциональные требования
           - Ограничения
           - Открытые вопросы

        Никогда не задавай вопросы после того, как выдал [FINAL_TZ].
    """.trimIndent()
) {

    suspend fun sendUserMessage(userInput: String): String {
        history.addUserMessage(userInput)

        val builtPrompt = prompt("requirements_chat") {
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

        history.addAssistantMessage(replyText)

        return replyText
    }
}

