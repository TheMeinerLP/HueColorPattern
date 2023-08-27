import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.onelitefeather.rockbrush"
version = "0.0.1-SNAPSHOT" // Change

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    implementation(platform("com.intellectualsites.bom:bom-newest:1.37"))
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("cloud.commandframework:cloud-core:1.8.3")
    implementation("cloud.commandframework:cloud-bukkit:1.8.3")
    implementation("cloud.commandframework:cloud-annotations:1.8.3")
    implementation("cloud.commandframework:cloud-paper:1.8.3")
    implementation("cloud.commandframework:cloud-brigadier:1.8.3")

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
    shadowJar {
        archiveFileName.set("${rootProject.name}.${archiveExtension.getOrElse("jar")}")
        // https://github.com/johnrengelman/shadow/issues/107
        isZip64 = true
        // relocate("net.kyori", "$group.net.kyori")
    }
}

paper {
    name = "Rockbrush"
    main = "net.onelitefeather.rockbrush.RockBrushEntry"
    apiVersion = "1.19"
    serverDependencies {
        register("FastAsyncWorldEdit") {
            hasOpenClassloader = true
            required = true
            joinClasspath = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}

