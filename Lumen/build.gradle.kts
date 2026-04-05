plugins {
    id("java")
}

group = "xyz.carmine.lumen"
version = "0.1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.5.32")
}