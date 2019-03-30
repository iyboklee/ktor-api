package com.github.iyboklee.repository

import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist
import com.github.iyboklee.model.Song

interface SongRepository {

    suspend fun findById(seq: Long): Song?

    suspend fun findByTitle(title: String): List<Song>

    suspend fun findAll(album: Album): List<Song>

    suspend fun findAll(artist: Artist): Map<String, List<Song>>

}
