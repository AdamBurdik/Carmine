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

    implementation("com.google.guava:guava:33.5.0-jre")
    implementation("net.bytebuddy:byte-buddy:1.18.8")

    implementation("com.electronwill.night-config:core:3.8.4")
    implementation("com.electronwill.night-config:toml:3.8.4")
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveVersion.set("")

    mergeServiceFiles()
    manifest {
        attributes("Main-Class" to "xyz.carmine.raven.Raven")
    }
}