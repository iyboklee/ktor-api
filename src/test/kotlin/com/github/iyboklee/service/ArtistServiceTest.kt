package com.github.iyboklee.service

import com.github.iyboklee.Jackson
import com.github.iyboklee.database
import com.github.iyboklee.repository.impl.JdbcArtistRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArtistServiceTest {

  private val log = LoggerFactory.getLogger(javaClass)

  private val objectMapper = Jackson

  private val artistService: ArtistService

  init {
    database(name = "ArtistServiceTest", maximumPoolSize = 1)

    artistService = ArtistService(JdbcArtistRepository())
  }

  @Test
  fun `ID로 아티스트 조회가 가능하다`() = runBlocking {
    val artist = artistService.findById(1)!!
    assertThat(artist.name, `is`("TWICE"))
    val json = objectMapper.writeValueAsString(artist)
    log.info("Artist: $json")
  }

  @Test
  fun `이름으로 아티스트 조회가 가능하다`() = runBlocking {
    val artist = artistService.findByName("BTS")!!
    assertThat(artist.name, `is`("BTS"))
    val json = objectMapper.writeValueAsString(artist)
    log.info("Artist: $json")
  }

  @Test
  fun `모든 아티스트 조회가 가능하다`() = runBlocking {
    val artists = artistService.findAll()
    assertThat(artists.size, `is`(4))
    val json = objectMapper.writeValueAsString(artists)
    log.info("Artists: $json")
  }

}