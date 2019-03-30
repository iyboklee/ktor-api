package com.github.iyboklee.graphql

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist
import com.github.iyboklee.model.Song
import com.github.iyboklee.server.ApiServerContext
import com.github.iyboklee.server.toLocalDate
import com.github.iyboklee.server.yyyyMMdd
import com.github.pgutkowski.kgraphql.ExecutionException
import com.github.pgutkowski.kgraphql.KGraphQL
import com.github.pgutkowski.kgraphql.schema.Schema
import com.github.pgutkowski.kgraphql.schema.scalar.StringScalarCoercion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

fun initGraphQLSchema(context: ApiServerContext): Schema = KGraphQL.schema {
    configure {
        useDefaultPrettyPrinter = true
        objectMapper = jacksonObjectMapper()
        acceptSingleValueAsArray = true
        coroutineDispatcher = Dispatchers.IO
    }

    stringScalar<LocalDate> {
        coercion = object : StringScalarCoercion<LocalDate> {
            override fun serialize(instance: LocalDate) = instance.yyyyMMdd()
            override fun deserialize(raw: String) = raw.toLocalDate() ?: throw ExecutionException("Could not deserialize value: $raw to LocalDate")
        }
    }

    query("artists") {
        description = "Artist 목록"
        suspendResolver { offset: Int?, length: Int? ->
            context.artistService.findAll(offset ?: 0, length ?: 20)
        }
    }

    query("artist") {
        description = "Artist 조회"
        suspendResolver { seq: Long ->
            context.artistService.findById(seq)
        }
    }

    type<Artist> {
        description = "Artist 모델"
        property(Artist::seq) {
            description = "PK"
        }
        property(Artist::name) {
            description = "이름"
        }
        property(Artist::members) {
            description = "멤버수"
        }
        property(Artist::debutAt) {
            description = "데뷔일"
        }
        property<List<Album>>("albums") {
            resolver { artist: Artist ->
                runBlocking {
                    context.albumService.findAll(artist)
                }
            }
        }
    }

    type<Album> {
        property<List<Song>>("songs") {
            resolver { album: Album ->
                runBlocking {
                    context.songService.findAll(album)
                }
            }
        }
    }

    type<Song>()

    mutation("greeting") {
        description = "더미 mutation"
        resolver { name: String ->
            "hello, $name"
        }
    }
}