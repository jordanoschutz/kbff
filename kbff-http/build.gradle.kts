plugins {
    kotlin("jvm") version "1.7.22"
}

group = "me.jschutz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("io.ktor:ktor-client-mock:2.2.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    // todo: use Mockk or Mockito?
    // testImplementation("io.mockk:mockk:1.13.3")

    // Ktor HTTP client.
    implementation("io.ktor:ktor-client-java:2.2.2")

    // Jackson serializer/deserializer.
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
