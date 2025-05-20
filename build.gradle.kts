plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "org"
//version = "1.0-SNAPSHOT"
version = "1.1-NeoAI"
repositories {
    mavenCentral()
    maven { url = uri("https://www.jetbrains.com/intellij-repository/releases") }
    maven { url = uri("https://www.jetbrains.com/intellij-repository/snapshots") }
    maven { url = uri("https://cache-redirector.jetbrains.com/intellij-dependencies") }
}

//dependencies {
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
////    implementation("com.jetbrains.plugins:git4idea:2024.1.7")
//
////    implementation("org.eclipse.jgit:org.eclipse.jgit:5.13.0.202109080827-r")
//
//
//}
//dependencies {
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
//    implementation("org.eclipse.jgit:org.eclipse.jgit:5.13.0.202109080827-r")
//
//
//    // HTTP Client dependencies
//    implementation("io.ktor:ktor-client-core:2.1.3")
//    implementation("io.ktor:ktor-client-serialization:2.1.3")
//    implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
//    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")
//
////    implementation("com.jetbrains.intellij.idea:idea-IC:2021.3")
//}
dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.13.0.202109080827-r")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")



    // HTTP Client dependencies
    implementation("io.ktor:ktor-client-core:2.1.3")
    implementation("io.ktor:ktor-client-serialization:2.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20231013")
}



// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1.7")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf())
//    plugins.set(listOf("com.intellij.git"))
}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("252.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

}
