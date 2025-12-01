plugins {
    // Общая сборочная логика в convention-плагине.
    id("buildsrc.convention.kotlin-jvm")

    // Плагин приложения для сборки исполняемого JAR.
    application
}

dependencies {
    // Модульные зависимости
    implementation(project(":utils"))

    // Koog agents и дополнительные фичи
    implementation(libs.koog.agents)
    implementation(libs.koog.tools)
    implementation(libs.koog.features.event.handler)

    // Глушим предупреждения SLF4J, подключив no-op провайдер
    implementation(libs.slf4j.nop)
}

application {
    // Главный класс приложения (функция main в App.kt)
    mainClass = "org.example.app.AppKt"
}

