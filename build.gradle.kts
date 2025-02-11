plugins {
    id("java")
}

group = "de.jxnxsdev"
version = "1.0.0-ReleaseT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("https://repo.thejocraft.net/releases/") {
        name = "tjcserver"
    }
    maven {
        url = uri("https://repo.somewhatcity.net/releases")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("de.pianoman911:mapengine-api:1.8.7")
    compileOnly("net.somewhatcity:boiler-api:3.0.1")

    // Jackson dependencies
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2") // Core library for JSON mapping
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2") // Low-level JSON parsing
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2") // Annotations for JSON
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2") // Java 8 Date/Time support

    compileOnly ("org.projectlombok:lombok:1.18.36")
    annotationProcessor ("org.projectlombok:lombok:1.18.36")

    compileOnly("org.bytedeco:javacv-platform:1.5.11")

    compileOnly("uk.co.caprica:vlcj:4.5.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
