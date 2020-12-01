import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
}

group = "pl.crejk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val vavrVersion = "0.10.2"
val kotestVersion = "4.3.1"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.vavr:vavr-kotlin:${vavrVersion}")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:${kotestVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
    javaParameters = true
    allWarningsAsErrors = false
    freeCompilerArgs = listOf("-Xinline-classes")
}

val compileTestKotlin: KotlinCompile by tasks

compileTestKotlin.kotlinOptions.apply {
    jvmTarget = "1.8"
    javaParameters = true
    allWarningsAsErrors = false
}
