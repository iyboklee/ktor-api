package com.github.iyboklee.server

import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch

abstract class BaseServer(private val context: ServerContext) : AutoCloseable {

    private val log = LoggerFactory.getLogger(javaClass)

    private val shutdown = CountDownLatch(1)

    fun start() {
        log.info("${context.name} starting...")

        registerShutdownHook()

        try {
            context.init()
            log.info("${context.name} started.")
        } catch (e: Exception) {
            log.error("${context.name} start failed: ${e.message}", e)
            throw e
        }
    }

    override fun close() {
        log.info("${context.name} shutting down...")
        context.close()
        log.info("${context.name} shutdown.")
    }

    fun awaitTermination() {
        shutdown.await()
    }

    private fun registerShutdownHook() {
        val mainThread = Thread.currentThread()
        Runtime.getRuntime().addShutdownHook(Thread {
            shutdown.countDown()
            try {
                mainThread.join()
            } catch (ignored: InterruptedException) {
            }
        })
    }

}