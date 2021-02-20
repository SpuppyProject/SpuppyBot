plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    application
}

repositories {
    jcenter()
    maven(url = "https://jitpack.io")
}

val exposedVersion:String = "0.29.1"
val logbackVersion: String = "0.9.26"

dependencies {
    //The discord related libraries
    implementation("net.dv8tion:JDA:4.2.0_228")
    implementation("com.sedmelluq:lavaplayer:1.3.71")

    //Expsed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    //logback
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    //other
    implementation("org.mariadb.jdbc:mariadb-java-client:2.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.projectlombok:lombok:1.18.16")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClassName = "com.github.yeoj34760.bot.BotAt"
}
