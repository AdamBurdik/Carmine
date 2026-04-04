subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        "implementation"("org.jetbrains:annotations:26.1.0")

        // JUnit
        "testImplementation"(platform("org.junit:junit-bom:5.10.0"))
        "testImplementation"("org.junit.jupiter:junit-jupiter")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")

        // Lombok
        "compileOnly"("org.projectlombok:lombok:1.18.44")
        "annotationProcessor"("org.projectlombok:lombok:1.18.44")

        // Lombok - Test
        "testCompileOnly"("org.projectlombok:lombok:1.18.44")
        "testAnnotationProcessor"("org.projectlombok:lombok:1.18.44")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }
}