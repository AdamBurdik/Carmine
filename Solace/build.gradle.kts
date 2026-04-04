plugins {
    id("java")
}

group = "xyz.carmine.solace"
version = "0.1.0"

repositories {
}

dependencies {
    implementation(project(":Lumen")) // Common utils

    implementation("redis.clients:jedis:7.4.0")
    implementation("net.dv8tion:JDA:6.4.0")
}