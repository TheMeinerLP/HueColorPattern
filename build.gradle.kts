plugins {
    id("java")
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

group = "net.onelitefeather"
version = "0.0.1-SNAPSHOT" // Change

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
    jar {
        this.archiveVersion.set("")
    }
}

paper {
    name = "Playground"
    main = "net.onelitefeather.rockbrush.RockBrushEntry"
    apiVersion = "1.20"
}

