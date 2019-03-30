package com.github.iyboklee.repository.impl

import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist
import com.github.iyboklee.model.Song
import com.github.iyboklee.repository.Database.execute
import com.github.iyboklee.repository.SongRepository
import com.github.iyboklee.server.toJavaLocalDate
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select

class JdbcSongRepository : SongRepository {

    override suspend fun findById(seq: Long): Song? = execute {
        Songs.select {
            Songs.seq eq seq
        }.mapNotNull { row ->
            Song(
                seq = row[Songs.seq],
                title = row[Songs.title]
            )
        }.singleOrNull()
    }

    override suspend fun findByTitle(title: String): List<Song> = execute {
        Songs.select {
            Songs.title eq title
        }.mapNotNull { row ->
            Song(
                seq = row[Songs.seq],
                title = row[Songs.title]
            )
        }
    }

    override suspend fun findAll(album: Album): List<Song> = execute {
        Songs.select {
            Songs.albumSeq eq album.seq
        }.mapNotNull { row ->
            Song(
                seq = row[Songs.seq],
                title = row[Songs.title]
            )
        }
    }

    override suspend fun findAll(artist: Artist): Map<String, List<Song>> = execute {
        (Songs innerJoin Albums).select {
            Songs.artistSeq eq artist.seq
        }.mapNotNull { row ->
            Pair(
                first = row[Albums.title],
                second = Song(
                    seq = row[Songs.seq],
                    title = row[Songs.title]
                )
            )
        }.groupBy({ it.first }, { it.second })
    }
}

object Songs : Table() {
    val seq: Column<Long> = long("seq").autoIncrement().primaryKey()
    val artistSeq: Column<Long> = (long("artist_seq") references Artists.seq).index("fk_songs_to_artist")
    val albumSeq: Column<Long> = (long("album_seq") references Albums.seq).index("fk_songs_to_album")
    val title: Column<String> = varchar("title", 50)
}