package d2.chat

import d1.chat.AnimationType
import d1.chat.LoadingIndicator
import d2.agent.TypedAiAgentService
import kotlinx.coroutines.runBlocking

/**
 * Терминальный чат для d2, использующий TypedAiAgentService и тип AiAnswer.
 *
 * На каждый ввод пользователя:
 * - отправляет промпт в типизированного агента;
 * - показывает индикатор загрузки;
 * - выводит result, original и вопросы из AiAnswer;
 * - сохраняет ход диалога в TypedChatHistory.
 */
class TypedTerminalChatRunner(
    private val agentService: TypedAiAgentService,
    private val history: TypedChatHistory
) {

    private val loadingIndicator = LoadingIndicator()

    fun run() = runBlocking {
        println("=== Terminal OpenAI Chat (Koog, d2 / Typed AiAnswer) ===")
        println("Введите сообщение и нажмите Enter. Для выхода напишите: exit")
        println()

        while (true) {
            print("you> ")
            val userInput = readLine() ?: break

            if (userInput.equals("exit", ignoreCase = true)) {
                println("chat> Завершение работы. Пока!")
                break
            }

            if (userInput.isBlank()) {
                continue
            }

            try {
                // Показываем индикатор во время обращения к AI
                val answer = loadingIndicator.withLoading(
                    message = "AI думает (типизированный ответ)...",
                    animationType = AnimationType.SPINNER
                ) {
                    agentService.ask(userInput)
                }

                // Сохраняем шаг диалога в историю
                history.addTurn(userInput, answer)

                println()
                println("ai.result   > ${answer.result ?: "<пусто>"}")
                println("ai.original > ${"%.1f".format(answer.original)} %")

                if (answer.questions.isNotEmpty()) {
                    println("ai.questions:")
                    answer.questions.forEachIndexed { index, q ->
                        println("  ${index + 1}) $q")
                    }
                } else {
                    println("ai.questions > (нет уточняющих вопросов)")
                }
                println()
            } catch (e: Exception) {
                println()
                println("ERROR: Произошла ошибка при обращении к OpenAI: ${e.message}")
                println()
            }
        }
    }
}

