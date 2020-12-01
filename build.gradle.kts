import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
}

group = "pl.crejk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.3.1")
    testImplementation("io.kotest:kotest-runner-console-jvm:4.3.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}
