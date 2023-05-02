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
        relocate("org.bstats", "me.instantlymoist.NES.bstats")
        relocate("net.coobird", "me.instantlymoist.NES.coobird")
        relocate("com.github.retrooper.packetevents", "me.instantlymoist.NES.retrooper")
        relocate("io.github.retrooper.packetevents", "me.instantlymoist.NES.retrooper")
    }
}

tasks.register("putIntoTestServer") {
    dependsOn("shadowJar")
    val testServer = file("${projectDir}/server/plugins").apply{ if (!exists()) mkdirs() }
    doLast {
        copy {
            from("build/libs/NES-${project.version}.jar")
            into(testServer)
        }
    }
}