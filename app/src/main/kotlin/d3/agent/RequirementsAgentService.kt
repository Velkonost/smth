package d3.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import d3.model.BusinessRequirements
import d3.tools.AskUserTool
import kotlin.uuid.ExperimentalUuidApi

/**
 * Агент, который в диалоге (через инструмент ask_user) собирает требования,
 * а затем возвращает итоговый объект BusinessRequirements.
 */
class RequirementsAgentService(
    apiKey: String
) {

    // Регистрируем инструмент ask_user в ToolRegistry,
    // чтобы стратегия могла его вызывать.
    private val toolRegistry = ToolRegistry {
        tool(AskUserTool)
    }
    private val executor = simpleOpenAIExecutor(apiKey)

    @OptIn(ExperimentalUuidApi::class)
    private val agent: AIAgent<String, BusinessRequirements> =
        AIAgent(
            promptExecutor = executor,
            strategy = requirementsStrategy,
            agentConfig = AIAgentConfig(
                prompt = prompt("requirements-agent-prompt") {
                    system(
                        """
                            Ты — бизнес-аналитик.
                            Пользователь даёт исходное требование.
                            
                            Твоя задача:
                            1. При необходимости задавать уточняющие вопросы через инструмент `ask_user`.
                               Вызывай этот инструмент столько раз, сколько нужно, пока тебе не станет ясно ТЗ.
                            2. Когда информации достаточно, ПРЕКРАТИ вызывать инструмент и верни ИТОГОВОЕ ТЗ
                               строго как структурированный объект BusinessRequirements.
                        """.trimIndent()
                    )
                },
                model = OpenAIModels.Chat.GPT4o,
                maxAgentIterations = 30
            ),
            toolRegistry = toolRegistry
        )

    /**
     * Запускает диалоговый агент и сразу возвращает BusinessRequirements.
     */
    suspend fun buildRequirements(initialRequirement: String): BusinessRequirements {
        return agent.run(initialRequirement)
    }
}

