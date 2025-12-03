package d3.tools

import ai.koog.agents.core.tools.SimpleTool
import ai.koog.agents.core.tools.ToolArgs
import ai.koog.agents.core.tools.ToolDescriptor
import ai.koog.agents.core.tools.ToolParameterDescriptor
import ai.koog.agents.core.tools.ToolParameterType
import ai.koog.agents.core.tools.annotations.LLMDescription
import d1.chat.AnimationType
import d1.chat.LoadingIndicator
import kotlinx.serialization.Serializable

/**
 * Инструмент, который позволяет модели задавать вопрос пользователю в терминале
 * и получать текстовый ответ.
 *
 * Используем SimpleTool — как в примерах Koog, без ограничений на тип результата.
 */
@LLMDescription("Инструмент для уточнения требований: задаёт вопрос пользователю и возвращает его ответ.")
object AskUserTool : SimpleTool<AskUserTool.Args>() {

    @Serializable
    data class Args(
        @property:LLMDescription("Текст вопроса, который нужно задать пользователю.")
        val question: String
    ) : ToolArgs

    private val loadingIndicator = LoadingIndicator()

    // Сериализатор аргументов
    override val argsSerializer = Args.serializer()

    override val descriptor: ToolDescriptor = ToolDescriptor(
        name = "ask_user",
        description = "Инструмент для уточнения требований: задаёт вопрос пользователю и возвращает его ответ.",
        requiredParameters = listOf(
            ToolParameterDescriptor(
                name = "question",
                description = "Текст вопроса, который нужно задать пользователю.",
                type = ToolParameterType.String
            )
        )
    )

    // Основная логика инструмента
    override suspend fun doExecute(args: Args): String {
        println()
//        val answer = loadingIndicator.withLoading(
//            message = "AI формулирует вопрос...",
//            animationType = AnimationType.SPINNER
//        ) {
        println("ai(question)> ${args.question}")
        print("you(answer)> ")
        val answer = readLine() ?: ""
//        }
        return answer
    }
}

