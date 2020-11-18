plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    application
}

repositories {
    jcenter()
    maven(url = "https://jitpack.io")
}

dependencies {
    //The discord related libraries
    implementation("net.dv8tion:JDA:4.2.0_217")
    implementation("com.github.Devoxin:lavaplayer:1.3.61")

    //other
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClassName = "com.github.yeoj34760.bot.BotAt"
}
