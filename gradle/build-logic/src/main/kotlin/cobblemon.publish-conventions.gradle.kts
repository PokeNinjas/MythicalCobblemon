import utilities.VersionType
import utilities.writeVersion

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("dev.architectury.loom")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven("https://maven.mythicalnetwork.com/releases/") {
            name = "Mythical-Repo"
            credentials {
                username = (project.findProperty("mythical.auth.username") ?: project.findProperty("mythicalUsername") ?: System.getenv("MYTHICAL_USERNAME") ?: "") as String?
                password = (project.findProperty("mythical.auth.password") ?: project.findProperty("mythicalPassword") ?: System.getenv("MYTHICAL_PASSWORD") ?: "") as String?
            }
        }
    }

    publications {
        create<MavenPublication>(project.name) {
            artifact(tasks.remapJar)
            artifact(tasks.remapSourcesJar)

            @Suppress("UnstableApiUsage")
            loom.disableDeprecatedPomGeneration(this)

            groupId = "com.cobblemon"
            artifactId = "MythicalCobblemon"
            version = project.writeVersion(VersionType.PUBLISHING)
        }
    }
}