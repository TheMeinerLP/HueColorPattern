import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"

}

group = "net.onelitefeather.gradientpattern"
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
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.20.1")
    }
}

paper {
    name = "GradientBrush"
    main = "net.onelitefeather.gradientpattern.GradientPatternEntry"
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

