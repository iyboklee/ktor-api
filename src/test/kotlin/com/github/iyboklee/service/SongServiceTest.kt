package com.github.iyboklee.service

import com.github.iyboklee.Jackson
import com.github.iyboklee.database
import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist
import com.github.iyboklee.repository.impl.JdbcSongRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SongServiceTest {

    private val log = LoggerFactory.getLogger(javaClass)

    private val objectMapper = Jackson

    private val songService: SongService

    init {
        database(name = "SongServiceTest", maximumPoolSize = 1)

        songService = SongService(JdbcSongRepository())
    }

    @Test
    fun `ID로 노래 조회가 가능하다`() = runBlocking {
        val song = songService.findById(1)!!
        assertThat(song.title, `is`("YES or YES"))
        val json = objectMapper.writeValueAsString(song)
        log.info("Song: $json")
    }

    @Test
    fun `타이틀로 노래 조회가 가능하다`() = runBlocking {
        val songs = songService.findByTitle("뚜두뚜두 (DDU-DU DDU-DU)")
        assertThat(songs.size, `is`(1))
        assertThat(songs[0].title, `is`("뚜두뚜두 (DDU-DU DDU-DU)"))
        val json = objectMapper.writeValueAsString(songs)
        log.info("Song: $json")
    }

    @Test
    fun `앨범의 노래 조회가 가능하다`() = runBlocking {
        val songs = songService.findAll(Album(12, "Welcome to MOMOLAND", "댄스", LocalDate.of(2016, 11, 10)))
        assertThat(songs.size, `is`(3))
        assertThat(songs[0].title, `is`("짠쿵쾅"))
        assertThat(songs[1].title, `is`("상사병"))
        assertThat(songs[2].title, `is`("어기여차"))
        val json = objectMapper.writeValueAsString(songs)
        log.info("Song: $json")
    }

    @Test
    fun `아티스트의 노래 조회가 가능하다`() = runBlocking {
        val albumMap = songService.findAll(Artist(1, "TWICE", 9, LocalDate.of(2015, 10, 20)))
        assertThat(albumMap.size, `is`(3))
        assertThat(albumMap.values.map { it.size }.sum(), `is`(9))
        val json = objectMapper.writeValueAsString(albumMap)
        log.info("Albums: $json")
    }

}