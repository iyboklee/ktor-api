package com.github.iyboklee.server

import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.concurrent.TimeUnit

fun main(args : Array<String>) {
    val server = embeddedServer(Netty, commandLineEnvironment(args)).start()
    application!!.use {
        it.start()
        it.awaitTermination()
        server.stop(3, 5, TimeUnit.SECONDS)
    }
}