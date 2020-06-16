plugins {
    application
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    mavenCentral()
    jcenter()
}

val ktorVersion = "1.3.2"

dependencies {

    // Just regular Kotlin setup
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")

    // A server engine
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    // Optional features
    implementation("io.ktor:ktor-serialization:$ktorVersion")        // Content negotiation & serialization
    implementation("io.ktor:ktor-auth:$ktorVersion")                 // Authentication
    implementation("io.ktor:ktor-html-builder:$ktorVersion")         // Html builders
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion") // Test server engine

    // Some tools work well with Ktor. This one is a dependency injection framework.
    implementation("org.koin:koin-ktor:2.1.6")

    // Logger
    runtimeOnly("org.slf4j:slf4j-simple:1.7.30")
}

application {
    mainClassName = "com.github.jcornaz.demo.ktor.MainKt"
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
