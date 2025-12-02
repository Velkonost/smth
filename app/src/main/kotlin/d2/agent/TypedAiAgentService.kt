package d2.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import d2.model.AiAnswer
import kotlin.uuid.ExperimentalUuidApi

/**
 * Сервис над Koog AIAgent, который всегда возвращает строго типизированный AiAnswer.
 *
 * Внутри используется:
 * - AIAgent + strategy с subgraph (см. typedAnswerStrategy)
 * - simpleOpenAIExecutor для работы с OpenAI через Koog
 */
class TypedAiAgentService(
    apiKey: String
) {

    // Пока не используем никакие инструменты, но Registry обязателен.
    private val toolRegistry = ToolRegistry { }

    // Простой executor для OpenAI моделей
    private val executor = simpleOpenAIExecutor(apiKey)

    // Агент Koog, который будет исполнять нашу стратегию.
    // Обрати внимание: generic-параметр результата соответствует AiAnswer.
    @OptIn(ExperimentalUuidApi::class)
    private val agent: AIAgent<String, AiAnswer> =
        AIAgent(
            promptExecutor = executor,
            strategy = typedAnswerStrategy,
            agentConfig = AIAgentConfig(
                prompt = prompt("typed-answer-prompt") {
                    system(
                        """
                            Ты — помощник, который всегда отвечает в строго структурированной форме AiAnswer.
                            
                            Поле `result` — краткий, понятный человеку ответ на исходный промпт.
                            Поле `original` — число с плавающей точкой от 0.0 до 100.0, отражающее оригинальность промпта:
                              - 0.0–30.0  — типичный, банальный запрос;
                              - 30.0–70.0 — средняя оригинальность;
                              - 70.0–100.0 — очень оригинальный, нестандартный запрос.
                            Поле `questions` — список уточняющих вопросов к пользователю, помогающих лучше понять задачу.
                            
                            Всегда заполняй все поля: если нет итогового ответа, оставь result = null,
                            если нет вопросов — возвращай пустой список.
                        """.trimIndent()
                    )
                },
                model = OpenAIModels.Chat.GPT4o,
                maxAgentIterations = 10
            ),
            toolRegistry = toolRegistry
        )

    /**
     * Отправляет вопрос в агента и возвращает строго типизированный ответ AiAnswer.
     */
    suspend fun ask(question: String): AiAnswer {
        // Koog-агент теперь возвращает строго типизированный AiAnswer.
        return agent.run(question)
    }
}
