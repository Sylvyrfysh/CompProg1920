import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "doesnt.matter"
version = "0.2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
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