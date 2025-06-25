plugins {
    kotlin("jvm") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Android dependencies
    implementation("androidx.core:core-ktx:1.10.1")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.0")
}

tasks.test {
    useJUnit()
}