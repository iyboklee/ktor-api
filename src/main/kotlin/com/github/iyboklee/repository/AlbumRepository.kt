package com.github.iyboklee.repository

import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist

interface AlbumRepository {

  suspend fun findById(seq: Long): Album?

  suspend fun findByTitle(title: String): List<Album>

  suspend fun findByGenreLike(genre: String, offset: Int, length: Int): List<Album>

  suspend fun findAll(artist: Artist): List<Album>

}