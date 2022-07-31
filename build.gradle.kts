plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
}

group = "ru.crazylegend"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")
    implementation("com.j256.ormlite:ormlite-core:6.1")
    implementation("com.google.mug:mug:6.3")
}

publishing {

    repositories {
        maven {
            name = "oneklo"
            url = uri(
                "https://repo.oneklo.ru/private"
            )
            credentials {
                username = System.getenv("ONEKLO_USER")
                password = System.getenv("ONEKLO_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("voltage-database") {
            groupId = "ru.crazylegend"
            artifactId = "voltage-database"
            version = "1.1"

            from(components["java"])
        }
    }
}
