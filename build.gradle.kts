import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vertxVersion = "4.3.3"

plugins {
    kotlin("jvm") version "1.7.10"
    id("io.vertx.vertx-plugin") version "1.3.0"
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

vertx {
    mainVerticle = "com.ahmetduruer.huuxy.Main"
    vertxVersion = this@Build_gradle.vertxVersion
}

dependencies {
    /*
     * Runtime dependencies
     */

    implementation(kotlin("stdlib-jdk8"))

    implementation("io.vertx:vertx-tcp-eventbus-bridge:$vertxVersion")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")

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

    vertxDebug {
        environment("EnvironmentType", "DEVELOPMENT")
        environment("HuuxyVersion", fullVersion)
        environment("HuuxyBuildType", buildType)
    }

    vertxRun {
        environment("EnvironmentType", "DEVELOPMENT")
        environment("HuuxyVersion", fullVersion)
        environment("HuuxyBuildType", buildType)
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


application {
    mainClass.set("com.ahmetduruer.huuxy.Main")
}