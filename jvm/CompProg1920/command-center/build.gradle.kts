import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories { jcenter() }

    dependencies {
        classpath("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.3.50")
        classpath("org.jetbrains.kotlin", "kotlin-serialization", "1.3.50")
    }
}

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.3.50"
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "doesnt.matter"
version = "0.2.0"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-runtime", "0.13.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8" //we use J1.8 features
}

javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.fxml", "javafx.graphics")
}

application {
    mainClassName = "npj.Main"
}