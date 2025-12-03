package d3.model

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Question")
@LLMDescription("Уточняющий вопрос к пользователю в процессе сбора требований.")
data class Question(
    @property:LLMDescription("Текст вопроса.")
    val text: String
)

