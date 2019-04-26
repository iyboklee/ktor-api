package com.github.iyboklee.service

import com.github.iyboklee.Jackson
import com.github.iyboklee.database
import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist
import com.github.iyboklee.repository.AlbumRepository
import com.github.iyboklee.repository.impl.JdbcAlbumRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlbumServiceTest {

    private val log = LoggerFactory.getLogger(javaClass)

    private val objectMapper = Jackson

    private val albumService: AlbumService

    init {
        database(name = "AlbumServiceTest", maximumPoolSize = 1)

        albumService = AlbumService(JdbcAlbumRepository())
    }

    @Test
    fun `ID로 앨범 조회가 가능하다`() = runBlocking {
        val album = albumService.findById(4)!!
        assertThat(album.title, `is`("LOVE YOURSELF 結 Answer"))
        val json = objectMapper.writeValueAsString(album)
        log.info("Album: $json")
    }

    @Test
    fun `타이틀로 앨범 조회가 가능하다`() = runBlocking {
        val albums = albumService.findByTitle("YES or YES")
        assertThat(albums.size, `is`(1))
        assertThat(albums[0].title, `is`("YES or YES"))
        val json = objectMapper.writeValueAsString(albums)
        log.info("Albums: $json")
    }

    @Test
    fun `아티스트의 앨범 조회가 가능하다`() = runBlocking {
        val albums = albumService.findAll(Artist(3, "BLACKPINK", 4, LocalDate.of(2016, 8, 8)))
        assertThat(albums.size, `is`(3))
        assertThat(albums[2].title, `is`("SQUARE UP"))
        assertThat(albums[1].title, `is`("마지막처럼"))
        assertThat(albums[0].title, `is`("SQUARE TWO"))
        val json = objectMapper.writeValueAsString(albums)
        log.info("Albums: $json")
    }

    @Test
    fun `장르로 앨범을 검색할 수 있다`() = runBlocking {
        val albums = albumService.searchByGenre("랩")
        assertThat(albums.size, `is`(3))
        assertThat(albums[2].title, `is`("LOVE YOURSELF 結 Answer"))
        assertThat(albums[1].title, `is`("WINGS"))
        assertThat(albums[0].title, `is`("DARK&WILD"))
        val json = objectMapper.writeValueAsString(albums)
        log.info("랩 장르로 검색 결과: $json")

        val albums2 = albumService.searchByGenre("발라드")
        assertThat(albums2.size, `is`(1))
        assertThat(albums2[0].title, `is`("YES or YES"))
        val json2 = objectMapper.writeValueAsString(albums2)
        log.info("발라드 장르로 검색 결과: $json2")
    }

    @Test
    fun `아티스트와 제목으로 앨범을 검색할 수 있다`() = runBlocking {
        val mockRepository: AlbumRepository = mock {
            onBlocking {
                findAll(any())
            } doAnswer {
                listOf(
                    Album(seq = 1, title = "abcd", genre = "dance", issueAt = LocalDate.of(2019, 1, 1)),
                    Album(seq = 2, title = "ab12", genre = "pop", issueAt = LocalDate.of(2019, 2, 1)),
                    Album(seq = 3, title = "1bc2", genre = "rock", issueAt = LocalDate.of(2019, 3, 1)),
                    Album(seq = 4, title = "34bc", genre = "hip hop", issueAt = LocalDate.of(2019, 4, 1)),
                    Album(seq = 5, title = "efgh", genre = "ballade", issueAt = LocalDate.of(2019, 5, 1))
                )
            }
        }

        val service = AlbumService(mockRepository)

        val results1 = service.searchByArtistAndTitle(Artist(1, "test", 1, LocalDate.now()), "ab")
        assertThat(results1.size, `is`(2))
        assertThat(results1, `is`(
            listOf(
                Album(seq = 1, title = "abcd", genre = "dance", issueAt = LocalDate.of(2019, 1, 1)),
                Album(seq = 2, title = "ab12", genre = "pop", issueAt = LocalDate.of(2019, 2, 1))
            )
        ))
        val json = objectMapper.writeValueAsString(results1)
        log.info("키워드 `ab` 검색 결과: $json")

        val results2 = service.searchByArtistAndTitle(Artist(1, "test", 1, LocalDate.now()), "bc")
        assertThat(results2.size, `is`(3))
        assertThat(results2, `is`(
            listOf(
                Album(seq = 1, title = "abcd", genre = "dance", issueAt = LocalDate.of(2019, 1, 1)),
                Album(seq = 3, title = "1bc2", genre = "rock", issueAt = LocalDate.of(2019, 3, 1)),
                Album(seq = 4, title = "34bc", genre = "hip hop", issueAt = LocalDate.of(2019, 4, 1))
            )
        ))
        val json2 = objectMapper.writeValueAsString(results2)
        log.info("키워드 `bc` 검색 결과: $json2")
    }

}