plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "me.instantlymoist"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {

    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

    implementation("org.bstats:bstats-bukkit:1.7")
    implementation("net.coobird:thumbnailator:0.4.19")
    implementation("com.github.retrooper.packetevents:spigot:2.0-SNAPSHOT")

}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("NES-${project.version}.jar")
    }
}