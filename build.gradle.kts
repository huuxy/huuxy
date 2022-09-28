import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vertxVersion = "4.3.3"
val log4jVersion = "2.19.0"

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "com.ahmetduruer"
version = "1.0-SNAPSHOT"

val buildType = "alpha"
val timeStamp: String by project
val fullVersion = if (project.hasProperty("timeStamp")) "$version-$buildType-$timeStamp" else "$version-$buildType"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/iovertx-3720/")
    maven("https://jitpack.io")
}

dependencies {
    /*
     * Runtime dependencies
     */

    implementation(kotlin("stdlib-jdk8"))

    implementation("io.vertx:vertx-core:$vertxVersion")
    implementation("io.vertx:vertx-tcp-eventbus-bridge:$vertxVersion")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")

    implementation(group = "org.apache.logging.log4j", name = "log4j-api", version = log4jVersion)
    implementation(group = "org.apache.logging.log4j", name = "log4j-core", version = log4jVersion)
    implementation(group = "org.apache.logging.log4j", name = "log4j-slf4j-impl", version = log4jVersion)

    /*
     * TEST dependencies
     */

    testImplementation(kotlin("test"))
}

tasks {
    register("copyJar") {
        doLast {
            copy {
                from(shadowJar.get().archiveFile.get().asFile.absolutePath)
                into("./")
            }
        }

        dependsOn(shadowJar)
    }

    build {
        dependsOn("copyJar")
    }

    register("buildDev") {
        dependsOn("build")
    }

    shadowJar {
        manifest {
            val attrMap = mutableMapOf<String, String>()

            if (project.gradle.startParameter.taskNames.contains("buildDev"))
                attrMap["MODE"] = "DEVELOPMENT"

            attrMap["VERSION"] = fullVersion
            attrMap["BUILD_TYPE"] = buildType

            attributes(attrMap)
        }

        if (project.hasProperty("timeStamp")) {
            archiveFileName.set("Huuxy-${timeStamp}.jar")
        } else {
            archiveFileName.set("Huuxy.jar")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.named<JavaExec>("run") {
    environment("EnvironmentType", "DEVELOPMENT")
    environment("HuuxyVersion", fullVersion)
    environment("HuuxyBuildType", buildType)
}

application {
    mainClass.set("com.ahmetduruer.huuxy.Main")
}