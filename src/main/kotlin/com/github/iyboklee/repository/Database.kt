package com.github.iyboklee.repository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.sql.Connection
import org.jetbrains.exposed.sql.Database as Exposed

object Database {

    private val log = LoggerFactory.getLogger(javaClass)

    private lateinit var pool: HikariDataSource

    private lateinit var exposed: Exposed

    private lateinit var dispatcher: CoroutineDispatcher

    fun init(name: String, dispatcherPoolSize: Int = 5, poolConfigSupplier: () -> HikariConfig) {
        log.info("Database of $name starting...")

        val poolConfig = poolConfigSupplier()

        log.info(ReflectionToStringBuilder.toString(poolConfig, ToStringStyle.MULTI_LINE_STYLE))
        pool = HikariDataSource(poolConfig)
        exposed = Exposed.connect(pool)

        dispatcher = newFixedThreadPoolContext(dispatcherPoolSize, "$name-dispatcher-pool")

        log.info("Database of $name started.")
    }

    fun close() {
        pool.close()
    }

    suspend fun <T> execute(block: () -> T): T = execute(Connection.TRANSACTION_READ_COMMITTED, 3, block)

    suspend fun <T> execute(transactionIsolation: Int, repetitionAttempts: Int, block: () -> T): T = withContext(dispatcher) {
        transaction(transactionIsolation, repetitionAttempts) {
            block()
        }
    }

}