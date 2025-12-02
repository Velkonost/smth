package org.example.app

/**
 * Общий интерфейс для терминальных чат-приложений.
 *
 * Позволяет запускать разные реализации (d1, d2) без дублирования кода.
 */
fun interface TerminalChatApplication {
    fun run()
}


