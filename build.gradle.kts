import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenCentral()
    jcenter()
}

val ktorVersion = "1.3.2"

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
