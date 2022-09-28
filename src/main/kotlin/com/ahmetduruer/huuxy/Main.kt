package com.ahmetduruer.huuxy

import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.slf4j.LoggerFactory
import java.util.jar.Manifest

class Main : CoroutineVerticle() {
    companion object {
        private val options by lazy {
            VertxOptions()
        }

        private val vertx by lazy {
            Vertx.vertx(options)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            vertx.deployVerticle(Main())
        }

        val VERSION by lazy {
            try {
                val urlClassLoader = ClassLoader.getSystemClassLoader()
                val manifestUrl = urlClassLoader.getResourceAsStream("META-INF/MANIFEST.MF")
                val manifest = Manifest(manifestUrl)

                manifest.mainAttributes.getValue("VERSION").toString()
            } catch (e: Exception) {
                System.getenv("HuuxyVersion").toString()
            }
        }

        val logger by lazy {
            LoggerFactory.getLogger("Huuxy")
        }
    }

    override suspend fun start() {
        println(
            " _    _                        \n" +
                    "| |  | |                       \n" +
                    "| |__| |_   _ _   ___  ___   _ \n" +
                    "|  __  | | | | | | \\ \\/ / | | |\n" +
                    "| |  | | |_| | |_| |>  <| |_| |\n" +
                    "|_|  |_|\\__,_|\\__,_/_/\\_\\\\__, |\n" +
                    "                          __/ |\n" +
                    "                         |___/  v${VERSION}\n "
        )

        logger.info("Hello world!")
    }

    override suspend fun stop() {
        println("Stopping...")
    }
}