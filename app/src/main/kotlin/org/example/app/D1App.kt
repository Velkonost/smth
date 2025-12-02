package org.example.app

import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import d1.chat.ChatHistory
import d1.chat.OpenAIChatService
import d1.chat.TerminalChatRunner

/**
 * Отдельная точка входа для d1-версии терминального чата.
 */
fun main() {
    val apiKey = loadApiKey()

    val client = OpenAILLMClient(apiKey)
    val history = ChatHistory()
    val chatService = OpenAIChatService(client, history)
    val runner = TerminalChatRunner(chatService)

    runner.run()
}


