package org.example.app

import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import d1.chat.ChatHistory
import d1.chat.OpenAIChatService
import d1.chat.TerminalChatRunner
import java.util.Properties

/**
 * Точка входа приложения.
 *
 * Сейчас приложение запускает простой терминальный чат с OpenAI.
 */
fun main() {
    val apiKey = loadApiKey()

    // Настраиваем клиента и сервисы по простым шагам
    val client = OpenAILLMClient(apiKey)
    val history = ChatHistory()
    val chatService = OpenAIChatService(client, history)
    val runner = TerminalChatRunner(chatService)

    runner.run()
}

/**
 * Загружает API ключ из config.properties (classpath) или переменной окружения.
 *
 * Порядок приоритета:
 * 1) config.properties: OPENAI_API_KEY
 * 2) переменная окружения OPENAI_API_KEY
 */
private fun loadApiKey(): String {
    val props = Properties()

    // Пытаемся прочитать config.properties из ресурсов
    val resourceStream = Thread.currentThread()
        .contextClassLoader
        .getResourceAsStream("config.properties")

    val apiKeyFromFile = resourceStream?.use { stream ->
        props.load(stream)
        props.getProperty("OPENAI_API_KEY")
    }

    val apiKey = apiKeyFromFile ?: System.getenv("OPENAI_API_KEY")

    if (apiKey.isNullOrBlank()) {
        println("ERROR: API ключ не найден.")
        println("Установите его либо в app/src/main/resources/config.properties (ключ OPENAI_API_KEY),")
        println("либо в переменную окружения OPENAI_API_KEY и перезапустите приложение.")
        throw IllegalStateException("OPENAI_API_KEY is not configured")
    }

    return apiKey
}


