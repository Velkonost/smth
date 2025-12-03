package org.example.app

import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import d1.chat.ChatHistory
import d1.chat.OpenAIChatService
import d1.chat.TerminalChatRunner
import d2.agent.TypedAiAgentService
import d2.chat.TypedChatHistory
import d2.chat.TypedTerminalChatRunner

/**
 * Точка входа по умолчанию.
 *
 * Сейчас можно легко переключаться между d1 и d2, не дублируя общий код.
 */
fun main() {
    val apiKey = loadApiKey()

    // Реализация для d1
    val d1App = TerminalChatApplication {
        val client = OpenAILLMClient(apiKey)
        val history = ChatHistory()
        val chatService = OpenAIChatService(client, history)
        val runner = TerminalChatRunner(chatService)
        runner.run()
    }

    // Реализация для d2 (типизированный ответ AiAnswer)
    val d2App = TerminalChatApplication {
        val service = TypedAiAgentService(apiKey)
        val history = TypedChatHistory()
        val runner = TypedTerminalChatRunner(service, history)
        runner.run()
    }

    // По умолчанию запускаем d1-чат; d2 можно вызвать из другого main или переключателем
    d2App.run()
}

