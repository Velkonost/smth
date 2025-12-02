package d2.model

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Строго типизированный ответ от AI.
 *
 * Используем аннотации Koog и kotlinx.serialization, чтобы
 * модель могла стабильно возвращать структурированный объект.
 */
@Serializable
@SerialName("AiAnswer")
@LLMDescription("Структурированный ответ от AI, включающий результат, оригинальность промпта и уточняющие вопросы.")
data class AiAnswer(
    @property:LLMDescription("Результирующий ответ от AI, если он есть.")
    val result: String?,

    @property:LLMDescription("Процент оригинальности пользовательского промпта по мнению AI (0.0 - 100.0).")
    val original: Float,

    @property:LLMDescription("Список уточняющих вопросов от AI к пользователю.")
    val questions: List<String>
)

