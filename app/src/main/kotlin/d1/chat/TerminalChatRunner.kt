package d1.chat

import kotlinx.coroutines.runBlocking

/**
 * Класс, который отвечает только за терминальный REPL:
 * чтение строк, вывод подсказок и вызов сервиса.
 *
 * Здесь также используем LoadingIndicator во время ожидания ответа от AI.
 */
class TerminalChatRunner(
    private val chatService: OpenAIChatService
) {

    private val loadingIndicator = LoadingIndicator()

    fun run() = runBlocking {
        println("=== Terminal OpenAI Chat (Koog) ===")
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
                val reply = loadingIndicator.withLoading(
                    message = "AI думает",
                    animationType = AnimationType.SPINNER
                ) {
                    chatService.sendUserMessage(userInput)
                }

                println("ai> $reply")
            } catch (e: Exception) {
                println("ERROR: Произошла ошибка при обращении к OpenAI: ${e.message}")
            }
        }
    }
}


