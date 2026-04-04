plugins {
    id("java")
    id("com.gradleup.shadow") version "9.4.1"
}

group = "xyz.carmine.raven"
version = "0.1.0"

repositories {
}

dependencies {
    implementation(project(":Lumen")) // Common utils

    implementation("net.minestom:minestom:2026.03.25-1.21.11")

    implementation("redis.clients:jedis:7.4.0")
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveVersion.set("")

    mergeServiceFiles()
    manifest {
        attributes("Main-Class" to "xyz.carmine.raven.Raven")
    }
}