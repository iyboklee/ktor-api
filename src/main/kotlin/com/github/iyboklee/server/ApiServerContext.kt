package com.github.iyboklee.server

import com.github.iyboklee.graphql.initGraphQLSchema
import com.github.iyboklee.repository.Database
import com.github.iyboklee.repository.impl.JdbcAlbumRepository
import com.github.iyboklee.repository.impl.JdbcArtistRepository
import com.github.iyboklee.repository.impl.JdbcSongRepository
import com.github.iyboklee.service.AlbumService
import com.github.iyboklee.service.ArtistService
import com.github.iyboklee.service.SongService
import com.github.pgutkowski.kgraphql.schema.Schema
import com.zaxxer.hikari.HikariConfig
import io.ktor.application.Application

class ApiServerContext(application: Application) : ServerContext {

    val configuration = application.environment.config.config("service")

    val artistService: ArtistService by lazy {
        ArtistService(JdbcArtistRepository())
    }

    val albumService: AlbumService by lazy {
        AlbumService(JdbcAlbumRepository())
    }

    val songService: SongService by lazy {
        SongService(JdbcSongRepository())
    }

    val schema: Schema by lazy {
        initGraphQLSchema(this)
    }

    override fun init() {
        val databaseConfig = configuration.config("database")
        val databaseName = databaseConfig.property("poolName").getString()

        Database.init(
            name = databaseName,
            dispatcherPoolSize = databaseConfig.property("dispatcherPoolSize").getString().toInt()
        ) {
            HikariConfig().apply {
                poolName = databaseName
                driverClassName = databaseConfig.property("driverClassName").getString()
                jdbcUrl = databaseConfig.property("jdbcUrl").getString()
                username = databaseConfig.property("username").getString()
                password = databaseConfig.property("password").getString()
                minimumIdle = databaseConfig.property("minimumIdle").getString().toInt()
                maximumPoolSize = databaseConfig.property("maximumPoolSize").getString().toInt()
            }
        }
    }

    override fun close() {
        Database.close()
    }

    override val name: String
        get() = configuration.property("name").getString()

}