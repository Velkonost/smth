package org.example.app

import d2.agent.TypedAiAgentService
import d2.chat.TypedTerminalChatRunner

/**
 * Отдельная точка входа для d2-версии терминального чата с типизированным ответом AiAnswer.
 */
fun main() {
    val apiKey = loadApiKey()

    val service = TypedAiAgentService(apiKey)
    val runner = TypedTerminalChatRunner(service)

    runner.run()
}


