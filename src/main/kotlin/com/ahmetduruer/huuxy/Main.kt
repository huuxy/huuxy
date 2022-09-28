package com.ahmetduruer.huuxy

import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.kotlin.coroutines.CoroutineVerticle

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
    }

    override suspend fun start() {
        println("Hello world!")
    }

    override suspend fun stop() {
        println("Stopping...")
    }
}