package com.github.iyboklee.repository.impl

import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist
import com.github.iyboklee.repository.AlbumRepository
import com.github.iyboklee.repository.Database.execute
import com.github.iyboklee.server.toJavaLocalDate
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime

class JdbcAlbumRepository : AlbumRepository {

  override suspend fun findById(seq: Long): Album? = execute {
    Albums.select {
      Albums.seq eq seq
    }.mapNotNull { row ->
      Album(
          seq = row[Albums.seq],
          title = row[Albums.title],
          genre = row[Albums.genre],
          issueAt = row[Albums.issueAt].toJavaLocalDate()
      )
    }.singleOrNull()
  }

  override suspend fun findByTitle(title: String): List<Album> = execute {
    Albums.select { Albums.title eq title }
      .orderBy(Albums.seq, SortOrder.DESC)
      .mapNotNull { row ->
        Album(
            seq = row[Albums.seq],
            title = row[Albums.title],
            genre = row[Albums.genre],
            issueAt = row[Albums.issueAt].toJavaLocalDate()
        )
      }
  }

  override suspend fun findByGenreLike(genre: String, offset: Int, length: Int): List<Album> = execute {
    Albums.select { Albums.genre like "%$genre%" }
      .orderBy(Albums.seq, SortOrder.DESC)
      .limit(length, offset = offset)
      .mapNotNull { row ->
        Album(
            seq = row[Albums.seq],
            title = row[Albums.title],
            genre = row[Albums.genre],
            issueAt = row[Albums.issueAt].toJavaLocalDate()
        )
      }
  }

  override suspend fun findAll(artist: Artist): List<Album> = execute {
    Albums.select { Albums.artistSeq eq artist.seq }
      .orderBy(Albums.seq, SortOrder.DESC)
      .mapNotNull { row ->
        Album(
            seq = row[Albums.seq],
            title = row[Albums.title],
            genre = row[Albums.genre],
            issueAt = row[Albums.issueAt].toJavaLocalDate()
        )
      }
  }

}

object Albums : Table() {
  val seq: Column<Long> = long("seq").autoIncrement().primaryKey()
  val artistSeq: Column<Long> = (long("artist_seq") references Artists.seq).index("fk_album_to_artist")
  val title: Column<String> = varchar("title", 50)
  val genre: Column<String?> = varchar("genre", 50).nullable()
  val issueAt: Column<DateTime> = date("issue_at")

  init {
    index("uq_artist_album_title", true, artistSeq, title)
  }
}