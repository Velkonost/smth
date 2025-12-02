package d2.agent

import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeLLMRequestStructured
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.structure.StructureFixingParser
import ai.koog.prompt.structure.StructuredResponse
import d2.model.AiAnswer

/**
 * Стратегия агента с одним подграфом (subgraph), который
 * отвечает за получение строго типизированного AiAnswer.
 *
 * Вход: String (промпт пользователя)
 * Выход: AiAnswer (структурированный объект).
 */
val typedAnswerStrategy = strategy<String, AiAnswer>("typed-answer-strategy") {

    // Подграф, который запрашивает структурированный ответ AiAnswer у модели
    val answerSubgraph by subgraph<String, AiAnswer>("answer_subgraph") {
        // Узел, который делает запрос к LLM и возвращает Result<StructuredResponse<AiAnswer>>
        val nodeGetStructuredAnswer by nodeLLMRequestStructured<AiAnswer>(
            name = "typed-answer-node",
            fixingParser = StructureFixingParser(
                fixingModel = OpenAIModels.Chat.GPT4o,
                retries = 2
            )
        )

        // Узел, который превращает Result<StructuredResponse<AiAnswer>> в сам AiAnswer
        val nodeToAiAnswer by node<Result<StructuredResponse<AiAnswer>>, AiAnswer> { result ->
            if (result.isSuccess) {
                // Если всё ок, берём структуру из ответа
                result.getOrThrow().structure
            } else {
                // Фоллбэк: возвращаем "пустой" объект, чтобы не падать
                AiAnswer(
                    result = null,
                    original = 0f,
                    questions = emptyList()
                )
            }
        }

        edge(nodeStart forwardTo nodeGetStructuredAnswer)
        edge(nodeGetStructuredAnswer forwardTo nodeToAiAnswer)
        edge(nodeToAiAnswer forwardTo nodeFinish)
    }

    // Основной граф: делегирует работу в подграф и завершает стратегию
    nodeStart then answerSubgraph then nodeFinish
}

