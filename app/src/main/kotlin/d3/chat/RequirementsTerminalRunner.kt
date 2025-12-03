package d3.chat

import d1.chat.AnimationType
import d1.chat.LoadingIndicator
import kotlinx.coroutines.runBlocking

/**
 * Терминальный чат для d3:
 * - пользователь вводит требования;
 * - модель задаёт уточняющие вопросы;
 * - при готовности модель выдаёт финальное ТЗ ([FINAL_TZ]) и чат завершается.
 */
class RequirementsTerminalRunner(
    private val chatService: RequirementsChatService,
    private val history: RequirementsChatHistory
) {

    private val loadingIndicator = LoadingIndicator()

    fun run() = runBlocking {
        println("=== Requirements Chat (d3 / сбор ТЗ) ===")
        println("Опишите требования. Модель будет задавать уточняющие вопросы.")
        println("Для выхода в любой момент напишите: exit")
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
                val reply = loadingIndicator.withLoading(
                    message = "AI уточняет требования...",
                    animationType = AnimationType.SPINNER
                ) {
                    chatService.sendUserMessage(userInput)
                }

                println()
                println("ai> $reply")
                println()

                // Если модель выдала финальное ТЗ — завершаем диалог
                if (reply.startsWith("[FINAL_TZ]")) {
                    println("chat> Модель собрала достаточное количество требований и вернула итоговое ТЗ. Диалог завершён.")
                    break
                }
            } catch (e: Exception) {
                println()
                println("ERROR: Произошла ошибка при обращении к OpenAI: ${e.message}")
                println()
            }
        }

        // При желании после завершения можно посмотреть размер истории:
        // println("Всего сообщений в истории: ${history.size()}")
    }
}

