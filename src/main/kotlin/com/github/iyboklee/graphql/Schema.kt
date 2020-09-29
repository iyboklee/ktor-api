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
      override fun deserialize(raw: String) =
        raw.toLocalDate() ?: throw ExecutionException("Could not deserialize value: $raw to LocalDate")
    }
  }

  query("artist") {
    description = "Artist(아티스트) PK로 조회"
    suspendResolver { seq: Long ->
      context.artistService.findById(seq)
    }
  }

  query("artistByName") {
    description = "Artist(아티스트) 이름으로 조회"
    suspendResolver { name: String ->
      context.artistService.findByName(name)
    }
  }

  query("artists") {
    description = "Artist(아티스트) 목록"
    suspendResolver { offset: Int?, length: Int? ->
      context.artistService.findAll(offset ?: 0, length ?: 20)
    }
  }

  query("album") {
    description = "Album(앨범) PK로 조회"
    suspendResolver { seq: Long ->
      context.albumService.findById(seq)
    }
  }

  query("albumsByGenre") {
    description = "Album(앨범) 장르로 검색"
    suspendResolver { genre: String, offset: Int?, length: Int? ->
      context.albumService.searchByGenre(genre, offset ?: 0, length ?: 20)
    }
  }

  query("song") {
    description = "Song(노래) PK로 조회"
    suspendResolver { seq: Long ->
      context.songService.findById(seq)
    }
  }

  query("songsByTitle") {
    description = "Song(노래) 제목으로 조회"
    suspendResolver { title: String ->
      context.songService.findByTitle(title)
    }
  }

  type<Artist> {
    description = "Artist(아티스트) 모델"
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
      description = "발매 앨범 목록"
      resolver { artist: Artist ->
        runBlocking {
          context.albumService.findAll(artist)
        }
      }
    }
  }

  type<Album> {
    description = "Album(앨범) 모델"
    property(Album::seq) {
      description = "PK"
    }
    property(Album::title) {
      description = "타이틀"
    }
    property(Album::genre) {
      description = "장르"
    }
    property(Album::issueAt) {
      description = "발매일"
    }
    property<List<Song>>("songs") {
      description = "수록곡 목록"
      resolver { album: Album ->
        runBlocking {
          context.songService.findAll(album)
        }
      }
    }
  }

  type<Song> {
    description = "Song(노래) 모델"
    property(Song::seq) {
      description = "PK"
    }
    property(Song::title) {
      description = "노래 제목"
    }
  }

  mutation("greeting") {
    description = "더미 mutation"
    resolver { name: String ->
      "hello, $name"
    }
  }
}