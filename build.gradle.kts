import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
    id("application")
}

application {
    mainClass.set("coffee.j4n.westonia.Westonia")
}

group = "coffee.j4n.westonia"
version = "Beta 1.0 MC1.21.1-DEV"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.16.2")
    // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    // https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
    implementation("com.mysql:mysql-connector-j:8.2.0")

    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}

tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

val serverDir = "C:\\Dev\\Minecraft Server\\Westonia 1.21.X"
val jarFile = layout.buildDirectory.file("libs/${project.name}-${project.version}-all.jar").get().asFile.absolutePath

tasks.register("updatePlugin") {
    group = "Westonia"
    dependsOn("shadowJar")
    doLast {
        exec {
            commandLine("powershell", "-NoProfile", "-ExecutionPolicy", "Bypass", "-File", "update_plugin.ps1", "-serverDir", serverDir, "-jarFile", jarFile)
        }
    }
}
