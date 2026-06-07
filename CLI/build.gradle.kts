plugins {
    id("java")
    id("com.gradleup.shadow") version "9.4.1"
}

group = "me.adamix.carmine.cli"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.hollowcube:polar:1.15.1")
    implementation("net.minestom:minestom:2026.03.25-1.21.11")
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveVersion.set("")

    mergeServiceFiles()

    manifest {
        attributes("Main-Class" to "xyz.carmine.cli.Main")
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}