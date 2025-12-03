package d3.model

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("BusinessRequirements")
@LLMDescription("Структурированное ТЗ / бизнес-требования по итогам диалога с пользователем.")
data class BusinessRequirements(
    @property:LLMDescription("Краткое название продукта или проекта.")
    val title: String,

    @property:LLMDescription("Краткое общее описание продукта.")
    val overview: String,

    @property:LLMDescription("Цели продукта, которых должен достичь бизнес.")
    val goals: List<String>,

    @property:LLMDescription("Ключевые функциональные требования (что продукт должен уметь).")
    val functionalRequirements: List<String>,

    @property:LLMDescription("Нефункциональные требования (производительность, безопасность, UX и т.п.).")
    val nonFunctionalRequirements: List<String>,

    @property:LLMDescription("Ограничения и предположения (бюджет, сроки, платформа, интеграции и т.п.).")
    val constraints: List<String>,

    @property:LLMDescription("Оставшиеся открытые вопросы, которые нужно уточнить позже.")
    val openQuestions: List<String>
)

