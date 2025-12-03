package org.example.app

import d2.agent.TypedAiAgentService
import d2.chat.TypedChatHistory
import d2.chat.TypedTerminalChatRunner

/**
 * Отдельная точка входа для d2-версии терминального чата с типизированным ответом AiAnswer.
 */
fun main() {
    val apiKey = loadApiKey()

    val service = TypedAiAgentService(apiKey)
    val history = TypedChatHistory()
    val runner = TypedTerminalChatRunner(service, history)

    runner.run()
}

