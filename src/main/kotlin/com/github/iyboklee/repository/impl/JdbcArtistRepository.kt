package com.github.iyboklee.repository.impl

import com.github.iyboklee.model.Artist
import com.github.iyboklee.repository.ArtistRepository
import com.github.iyboklee.repository.Database.execute
import com.github.iyboklee.server.toJavaLocalDate
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

class JdbcArtistRepository : ArtistRepository {

  override suspend fun findById(seq: Long): Artist? = execute {
    Artists.select {
      Artists.seq eq seq
    }.mapNotNull { row ->
      Artist(
          seq = row[Artists.seq],
          name = row[Artists.name],
          members = row[Artists.members],
          debutAt = row[Artists.debutAt].toJavaLocalDate()
      )
    }.singleOrNull()
  }

  override suspend fun findByName(name: String): Artist? = execute {
    Artists.select {
      Artists.name eq name
    }.mapNotNull { row ->
      Artist(
          seq = row[Artists.seq],
          name = row[Artists.name],
          members = row[Artists.members],
          debutAt = row[Artists.debutAt].toJavaLocalDate()
      )
    }.singleOrNull()
  }

  override suspend fun findAll(offset: Int, length: Int): List<Artist> = execute {
    Artists.selectAll()
      .limit(length, offset = offset)
      .orderBy(Artists.seq, SortOrder.DESC)
      .mapNotNull { row ->
        Artist(
            seq = row[Artists.seq],
            name = row[Artists.name],
            members = row[Artists.members],
            debutAt = row[Artists.debutAt].toJavaLocalDate()
        )
      }
  }

}

object Artists : Table() {
  val seq: Column<Long> = long("seq").autoIncrement().primaryKey()
  val name: Column<String> = varchar("name", 50).uniqueIndex("uq_artist_name")
  val members: Column<Int> = integer("members").default(1)
  val debutAt: Column<DateTime> = date("debut_at")
}