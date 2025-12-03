package d3.agent

import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeExecuteTool
import ai.koog.agents.core.dsl.extension.nodeLLMRequest
import ai.koog.agents.core.dsl.extension.nodeLLMSendToolResult
import ai.koog.agents.core.dsl.extension.onAssistantMessage
import ai.koog.agents.core.dsl.extension.onToolCall
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.structure.StructureFixingParser
import ai.koog.prompt.structure.StructuredResponse
import d3.model.BusinessRequirements
import d3.tools.AskUserTool

/**
 * Стратегия агента для d3 на графах с инструментом ask_user.
 *
 * Логика:
 * - LLM может многократно вызывать инструмент AskUserTool для уточняющих вопросов;
 * - после завершения цикла мы отдельно запрашиваем структурированный BusinessRequirements;
 * - стратегия возвращает сразу BusinessRequirements (без ручного JSON-парсинга).
 */
val requirementsStrategy = strategy<String, BusinessRequirements>("requirements-strategy") {

    // Подграф диалога с пользователем через инструмент ask_user
    val dialogSubgraph by subgraph<String, String>(
        name = "dialog_subgraph",
        tools = listOf(AskUserTool)
    ) {
        val nodeCallLLM by nodeLLMRequest(
            name = "call_llm",
            allowToolCalls = true
        )
        val nodeExecuteTool by nodeExecuteTool()
        val nodeSendToolResult by nodeLLMSendToolResult()

        edge(nodeStart forwardTo nodeCallLLM)
        edge(nodeCallLLM forwardTo nodeFinish onAssistantMessage { true })
        edge(nodeCallLLM forwardTo nodeExecuteTool onToolCall { true })
        edge(nodeExecuteTool forwardTo nodeSendToolResult)
        edge(nodeSendToolResult forwardTo nodeExecuteTool onToolCall { true })
        edge(nodeSendToolResult forwardTo nodeFinish onAssistantMessage { true })
    }

    // Узел, который после диалога запрашивает структурированный BusinessRequirements
    val buildRequirementsNode by node<String, BusinessRequirements> { _ ->
        val result: Result<StructuredResponse<BusinessRequirements>> = llm.writeSession {
            requestLLMStructured(
                fixingParser = StructureFixingParser(
                    fixingModel = OpenAIModels.Chat.GPT4o,
                    retries = 2
                )
            )
        }

        if (result.isSuccess) {
            result.getOrThrow().structure
        } else {
            BusinessRequirements(
                title = "Не удалось собрать ТЗ",
                overview = result.exceptionOrNull()?.message ?: "Неизвестная ошибка",
                goals = emptyList(),
                functionalRequirements = emptyList(),
                nonFunctionalRequirements = emptyList(),
                constraints = emptyList(),
                openQuestions = emptyList()
            )
        }
    }

    nodeStart then dialogSubgraph then buildRequirementsNode then nodeFinish
}

