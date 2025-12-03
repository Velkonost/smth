package org.example.app

import d3.agent.RequirementsAgentService
import kotlinx.coroutines.runBlocking

fun main() {
    val apiKey = loadApiKey()
    val service = RequirementsAgentService(apiKey)

    println("=== D3 Requirements Agent (Koog, tools + стратегия) ===")
    print("Опишите исходное требование: ")
    val initial = readLine().orEmpty()

    runBlocking {
        try {
            val requirements = service.buildRequirements(initial)
            println()
            println("ИТОГОВОЕ ТЗ (BusinessRequirements):")
            println(requirements)
        } catch (e: Exception) {
            println("ERROR: Не удалось построить BusinessRequirements: ${e.message}")
        }
    }
}

