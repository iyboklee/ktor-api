package com.github.iyboklee.serving

import com.github.iyboklee.server.ApiServerContext
import com.github.iyboklee.server.BadRequest
import com.github.iyboklee.server.NotFound
import com.github.iyboklee.service.AlbumService
import com.github.iyboklee.service.ArtistService
import com.github.iyboklee.service.SongService
import io.ktor.application.call
import io.ktor.http.decodeURLPart
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

fun Routing.restApis(context: ApiServerContext, prefix: String) {
    artists(prefix, { context.artistService }, { context.albumService }, { context.songService })
    albums(prefix, { context.albumService }, { context.songService })
    songs(prefix) {
        context.songService
    }
}

private fun Routing.artists(
    prefix: String,
    getArtistService: () -> ArtistService,
    getAlbumService: () -> AlbumService,
    getSongService: () -> SongService
) {
    route("$prefix/artists") {
        /**
         * 모든 아티스트 조회
         *
         * url: /{prefix}/artists?offset={offset}&length={length}
         */
        get {
            val offset = call.request.queryParameters["offset"]?.toInt() ?: 0
            val length = call.request.queryParameters["length"]?.toInt() ?: 20
            val artists = getArtistService().findAll(offset, length)
            call.respond(artists)
        }
        /**
         * ID로 아티스트 조회
         *
         * url: /{prefix}/artists/{seq}
         */
        get("/{seq}") {
            val seq = call.parameters["seq"]?.toLong() ?: throw BadRequest()
            val artist = getArtistService().findById(seq) ?: throw NotFound()
            call.respond(artist)
        }
        /**
         * 이름으로 아티스트 조회
         *
         * url: /{prefix}/artists/name/{name}
         */
        get("/name/{name}") {
            val name = call.parameters["name"]?.decodeURLPart() ?: throw BadRequest()
            val artist = getArtistService().findByName(name) ?: throw NotFound()
            call.respond(artist)
        }
        /**
         * 앨범 목록 조회
         *
         * url: /{prefix}/artists/{seq}/albums
         */
        get("/{seq}/albums") {
            val seq = call.parameters["seq"]?.toLong() ?: throw BadRequest()
            val artist = getArtistService().findById(seq) ?: throw NotFound()
            val albums = getAlbumService().findAll(artist)
            call.respond(albums)
        }
        /**
         * 타이틀로 앨범 검색
         *
         * url: /{prefix}/artists/{seq}/albums/title/{title}
         */
        get("/{seq}/albums/title/{title}") {
            val seq = call.parameters["seq"]?.toLong() ?: throw BadRequest()
            val title = call.parameters["title"]?.decodeURLPart() ?: throw BadRequest()
            val artist = getArtistService().findById(seq) ?: throw NotFound()
            val albums = getAlbumService().searchByArtistAndTitle(artist, title)
            call.respond(albums)
        }
        /**
         * 노래 목록 조회
         *
         * url: /{prefix}/artists/{seq}/songs
         */
        get("/{seq}/songs") {
            val seq = call.parameters["seq"]?.toLong() ?: throw BadRequest()
            val artist = getArtistService().findById(seq) ?: throw NotFound()
            val albumMap = getSongService().findAll(artist)
            call.respond(albumMap)
        }
    }
}

private fun Routing.albums(
    prefix: String,
    getAlbumService: () -> AlbumService,
    getSongService: () -> SongService
) {
    route("$prefix/albums") {
        /**
         * ID로 앨범 조회
         *
         * url: /{prefix}/albums/{seq}
         */
        get("/{seq}") {
            val seq = call.parameters["seq"]?.toLong() ?: throw BadRequest()
            val album = getAlbumService().findById(seq) ?: throw NotFound()
            call.respond(album)
        }
        /**
         * 타이틀로 앨범 조회
         *
         * url: /{prefix}/albums/title/{title}
         */
        get("/title/{title}") {
            val title = call.parameters["title"]?.decodeURLPart() ?: throw BadRequest()
            val albums = getAlbumService().findByTitle(title)
            call.respond(albums)
        }
        /**
         * 장르로 앨범 검색
         *
         * url: /{prefix}/albums/genre/{genre}
         */
        get("/genre/{genre}") {
            val genre = call.parameters["genre"]?.decodeURLPart() ?: throw BadRequest()
            val albums = getAlbumService().searchByGenre(genre)
            call.respond(albums)
        }
        /**
         * 앨범 노래 목록 조회
         *
         * url: /{prefix}/albums/{seq}/songs
         */
        get("/{seq}/songs") {
            val seq = call.parameters["seq"]?.toLong() ?: throw BadRequest()
            val album = getAlbumService().findById(seq) ?: throw NotFound()
            val songs = getSongService().findAll(album)
            call.respond(songs)
        }
    }
}

private fun Routing.songs(prefix: String, getSongService: () -> SongService) {
    route("$prefix/songs") {
        /**
         * ID로 노래 조회
         *
         * url: /{prefix}/songs/{seq}
         */
        get("/{seq}") {
            val seq = call.parameters["seq"]?.toLong() ?: throw BadRequest()
            val song = getSongService().findById(seq) ?: throw NotFound()
            call.respond(song)
        }
        /**
         * 타이틀로 노래 조회
         *
         * url: /{prefix}/songs/title/{title}
         */
        get("/title/{title}") {
            val title = call.parameters["title"]?.decodeURLPart() ?: throw BadRequest()
            val songs = getSongService().findByTitle(title)
            call.respond(songs)
        }
    }
}