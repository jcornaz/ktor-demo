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
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))

    // Chose an engine
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    // Add the optional feature you'd like to use
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")

    // Add a logger implementation
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
