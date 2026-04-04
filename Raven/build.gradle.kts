plugins {
    id("java")
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