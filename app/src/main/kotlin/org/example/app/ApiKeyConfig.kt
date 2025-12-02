package org.example.app

import java.util.Properties

/**
 * Общая функция для загрузки API ключа OpenAI.
 *
 * Порядок приоритета:
 * 1) config.properties (classpath), ключ OPENAI_API_KEY
 * 2) переменная окружения OPENAI_API_KEY
 */
fun loadApiKey(): String {
    val props = Properties()

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


