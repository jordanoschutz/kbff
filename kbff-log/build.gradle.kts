plugins {
    id("java")
}

group = "me.jschutz"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    // kbff-http.
    implementation(project(mapOf("path" to ":kbff-http")))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
